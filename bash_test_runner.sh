# !/bin/bash

#==============================================================================
# Runner for the DMARF project 
# Tests the basic set of algorithms against the  provided 
# instances.  
# 
# Instances defined as *.dat files in the provided input_folder.
# Results data files as *.algorithm.out files in the provided  output folder.
#
#
# Author: Peter Jessa
#==============================================================================

input_folder=$2
output_folder=$3
executable_path=$4

# The metrics used in the following algorithms (values in %)
confidances="85 90 95"
supports="85 90 95"

# Algorithms that will be used 
# 	0 : Weka Algorithm
# 	1 : Apriori NST
# 	2 : Binary Apriori CPU
# 	3 : Binary Apriori CPU OpenCL (1 allign)
# 	4 : Binary Apriori GPU OpenCL  (4 allign)
# 	5 : Binary Apriori GPU OpenCL  (64 allign)
algorithms=" 0 1 2 3"

# Make sure the folder exists!
if [ -d $input_folder ] ; then
	echo "Reading the $input_folder"
else
	echo "Could not find folder $input_folder"
	exit 1
fi

#TODO: make sure that $output_folder exists

# Get the files from folder
for file in $files; do

	echo "Proceeding with file: '$file' "
	
	# Preapare values for invocation
	
	# Run algorithm
	echo "Algorthm Started"
	
	
	echo "Algorthm Finished"
end