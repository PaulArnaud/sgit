sbt clean
sbt assembly
export PATH=$PATH:`pwd`/target/scala-2.13
mkdir test
cd test 
echo "================ COMMAND : sgit init ================"
sgit init
echo "================ COMMAND : touch lapin ================"
touch lapin
echo "================ COMMAND : sgit status ================"
sgit status
echo "================ COMMAND : sgit add lapin ================"
sgit add lapin
echo "================ COMMAND : sgit status ================"
sgit status
echo "================ COMMAND : sgit commit -m 'Lapin' ================"
sgit commit -m "Lapin"
echo "================ COMMAND : sgit status ================"
sgit status
echo "================ COMMAND : sgit log ================"
sgit log
echo "================ COMMAND : sgit log -p ================"
sgit log -p
echo "================ COMMAND : 'fsdjlfdjs' > lapin ================"
echo "fsdjlfdjs" > lapin
echo "================ COMMAND : 'fdskmfsdi' >> lapin ================"
echo "fdskmfsdi" >> lapin
echo "================ COMMAND : 'fdsidfpd' >> lapin ================"
echo "fdsidfpd" >> lapin
echo "================ COMMAND : sgit diff ================"
sgit diff
echo "================ COMMAND : sgit add . ================"
sgit add .
echo "================ COMMAND : sgit diff ================"
sgit diff
echo "================ COMMAND : sgit status ================"
sgit status
echo "================ COMMAND : sgit commit -m '2nd' ================"
sgit commit -m "2nd"
echo "================ COMMAND : sgit status ================"
sgit status
echo "================ COMMAND : sgit log ================"
sgit log
echo "================ COMMAND : sgit log -p ================"
sgit log -p
cd ..
sudo rm -R test