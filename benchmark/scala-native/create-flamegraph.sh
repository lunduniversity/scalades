# assumes (1) you have perf installed on a linux system
# sudo apt update && sudo apt install linux-tools-generic

# assumes (2) you have cloned https://github.com/brendangregg/FlameGraph
# into this folder ~/git/hub/FlameGraph/
# the svg flame graph is created in kernel.svg 

# assumes (3) that you have done 
# sbt run

sudo perf record -F 1000 -a -g ./target/scala-2.13/scala-native-out
sudo perf script > out.perf
~/git/hub/FlameGraph/stackcollapse-perf.pl out.perf > out.folded
~/git/hub/FlameGraph/flamegraph.pl out.folded > kernel.svg
