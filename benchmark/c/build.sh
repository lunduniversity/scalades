CMD="gcc main.c -lm -O3"
echo "Compiling with: $CMD" 
$($CMD)
echo "Binary output (size name): $(ls -sh a.out)"
