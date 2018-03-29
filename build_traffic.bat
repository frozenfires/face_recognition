call build.bat

echo 添加交警违法系统配置文件
cd buildline\traffic
xcopy application.properties ..\..\target\Ethinkface
cd ..\..

echo 打包完成

pause