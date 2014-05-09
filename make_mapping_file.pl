#!/usr/bin/perl
#

use warnings;
use strict;

my $dir = $ARGV[0];

print "#SampleID\tBarcodeSequence\tLinkerPrimerSequence\tInputFileName\tDescription\n";

foreach (glob "$dir/*_single.fa"){
    my $file = $_;
    $file =~ s/$dir\///;
    my $name = $file;
    $name =~ s/_single.fa//;
    $name =~ s/[^a-zA-Z\d]//g;
    print STDERR $file . "\n";
    print "$name\tAA\tAT\t$file\t$name\n";
    

}
