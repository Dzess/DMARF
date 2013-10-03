# !/bin/bash

#==============================================================================
# Runner for the DMARF project 
#
# Synopsis:
# 	Tests the basic set of algorithms against the  provided 
# instances.  
# 
# Input:
# 	Instances defined as *.dat files in the provided input_folder.
#
# Output:
# 	Results data files as *.algorithm_number.out files in the provided  output folder.
# 	Algorithm_number is a number from Algorithms table defining which algorithm
# 	will be used.
#
# Invocation:
# 	.\script input_folder output_folder
#
# Author: Peter Jessa
#==============================================================================

input_folder=$1
output_folder=$2
executable_path=$3

# Passed value reading
echo "Location of input folder '$input_folder'"
echo "Location of output folder '$output_folder'"
echo "Location of the executable '$executable_path'"

# The metrics used in the following algorithms: values in % of all supported set
confidences="85 90 95"
supports="85 90 95"

# Ram Limit for Heap Size in the JVM
ram_limit="1024m"

# Algorithms that will be used 
# 	0 : Weka Algorithm
# 	1 : Apriori NST
# 	2 : Binary Apriori CPU
# 	3 : Binary Apriori CPU OpenCL (1 allign)
# 	4 : Binary Apriori GPU OpenCL  (4 allign)
algorithms="0 1 2 3 4"

# Make sure the folder exists!
if [ -d $input_folder ] ; then
	echo "Reading the '$input_folder'"
else
	echo "Could not find folder $input_folder"
	exit 1
fi

# Make sure that $output_folder exists
if [ -d $output_folder ] ; then
	echo "There is output folder: '$output_folder'. Sure you want to proceed?"
else
	echo "Creating the '$output_folder'"
	mkdir $out_put_folder 2>/dev/null
fi


# Get the files from folder
files=`ls  $input_folder | grep .dat`
for file in $files; do
	file_path_input="$input_folder/$file"
	file_path_output="$output_folder/$file"
	echo "Proceeding with file: '$file' "
	for algorithm_number in $algorithms; do
		for support in $supports; do
			for confidence in $confidences; do
				# Prepare values for invocation
				output_file="$file_path_output.$support.$confidance.$algorithm_number.out"
				invocation="java -Xmx$ram_limit -jar $executable_path $file_path_input $output_file $support $confidence $algorithm_number"
				echo $invocation
				`$invocation`
			done
		done
	done
done