CMD="g++ main.cpp"
echo "Compiling with: $CMD" 
$($CMD)
echo "Binary output (size name): $(ls -sh a.out)"
