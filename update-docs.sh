rm -rf docs
sbt doc
cp -r target/scala-3.0.0-RC1/api/ docs
