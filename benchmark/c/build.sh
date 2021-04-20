CMD="gcc main.c -lm"
echo "Compiling with $CMD" 
$($CMD)
echo "Binary output: $(ls -sh a.out)"
