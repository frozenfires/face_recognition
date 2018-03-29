call build.bat

echo 添加rdp数据脚本
cd buildline\db_rdp
jar -uf ..\..\target\Ethinkface\etface.jar ./BOOT-INF/classes/db/V99__update.sql
cd ..\..

echo 打包完成

pause