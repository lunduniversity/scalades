SCALAVERSION=scala-3.0.0-RC3
sbt doc
ssh $LUCATID@fileadmin.cs.lth.se "rm -rf scalades/docs"
scp -r target/$SCALAVERSION/api $LUCATID@fileadmin.cs.lth.se:/Websites/Fileadmin/scalades/docs