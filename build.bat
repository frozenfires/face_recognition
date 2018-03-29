@echo off
set verno=
set /p verno=请输入版本号:
call mvn clean package
pause

mkdir .\target\Ethinkface
xcopy .\buildline\runtime .\target\Ethinkface /E /F /Y
xcopy .\*.dll .\target\Ethinkface /F /Y

xcopy .\target\etface.jar .\target\Ethinkface /Y

echo 添加版本信息%verno%到.\target\Ethinkface\version
echo %verno% > .\target\Ethinkface\version.ini

echo 打包完成

pause