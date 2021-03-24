sbt doc
ssh $LUCATID@fileadmin.cs.lth.se "rm -rf scalades/docs"
scp -r target/scala-3.0.0-RC1/api $LUCATID@fileadmin.cs.lth.se:/Websites/Fileadmin/scalades/docs