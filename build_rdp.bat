call build.bat

echo ���rdp���ݽű�
cd buildline\db_rdp
jar -uf ..\..\target\Ethinkface\etface.jar ./BOOT-INF/classes/db/V99__update.sql
cd ..\..

echo ������

pause