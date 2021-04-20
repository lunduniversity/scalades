MODEL=$(lscpu | sed -nr '/Model name/ s/.*:\s*(.*) @ .*/\1/p')
LANG="C++"
echo "Simulating M/M/1 model in $LANG on $MODEL ..."
./a.out
