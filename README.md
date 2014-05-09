bpipe-qiime_closed_otus
=======================

bpipe-qiime_pick_closed_otus

A pipeline to assign otu's for 16S reads from fastq files.

Uses nesoni clip for primer removal (custom ion torrent primers and linkers)

Uses a custom perl script to produce a valid qiime file mapping text file

Uses 16S database as specified at the top of the qiime_pipeline.groovy file.

Requires a variant of the bpipe script to handle module specification. If this is not used, then the required modules must be loaded in the relevant .bashrc script...

bpipe variant: slugger70-bpipe

You should use Andrew Lonsdale's vagrant-bpipe scripts to use a custom bpipe...
