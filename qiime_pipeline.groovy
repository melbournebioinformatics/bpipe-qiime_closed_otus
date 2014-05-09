load 'bpipe.config'

PRIMERS_FILE = '/vlsci/VR0288/shared/16S/References/all_primers.fna'
SILVA_REP_SET = '/vlsci/VR0288/shared/16S/References/Silva_111_post/rep_set/99_Silva_111_rep_set.fasta'
SILVA_OTU_MAP = '/vlsci/VR0288/shared/16S/References/Silva_111_post/otu_maps/99_Silva_111_otu_map.txt'

def get_name(filename) {

    def name = filename.split("/")[-1]

    return(name)

}


nesoni_clip = {
    doc "Trim the primers out of the raw reads and remove poor quality reads. Using nesoni clip"
    
    def BASENAME = get_name(input.prefix)
    produce(BASENAME+"_log.txt", BASENAME+"_single.fa"){
        exec "nesoni clip: --adaptor-file $PRIMERS_FILE --length 50 --fasta yes --gzip no $BASENAME reads: $input", "nesoni_clip"
    }
}

write_mapping_file = {
    produce("mapping.txt"){
        exec "/vlsci/VR0288/shared/16S/bpipe_dev/make_mapping_file.pl . > mapping.txt", "local"
    }
}

combine_fastas = {
    produce("combined_seqs.fna"){
        exec "add_qiime_labels.py -m mapping.txt -i . -c InputFileName", "small_qiime_script"
    }
}

qiime_pick_otus = {
    produce("otu_table.biom"){
        output.dir = "qiime_otu_dir"
        exec "pick_closed_reference_otus.py -i $input -r $SILVA_REP_SET -o $output.dir -t $SILVA_OTU_MAP -a -O 8 -f","qiime_pick_close_otus"
    }
}

qiime_get_obs_stats = {
    produce("library_stats_num_obs.txt"){
        output.dir = "qiime_otu_dir"
        exec "print_biom_table_summary.py --num_observations -i $input.biom -o $output","small_qiime_script"
    }
}

qiime_get_table_stats = {
    produce("library_stats.txt"){
        exec "print_biom_table_summary.py -i $input.biom -o $output","small_qiime_script"
    }
}

qiime_make_heatmap = {
    produce("otu_table.html"){
        output.dir = "otu_heatmap"
        exec "make_otu_heatmap_html.py -i $input.biom -o $output.dir","small_qiime_script"
    }
}

run {
    "%.fq" * [nesoni_clip] + write_mapping_file + combine_fastas + qiime_pick_otus + qiime_get_obs_stats + qiime_get_table_stats + qiime_make_heatmap
}
