; 该脚本使用 HM VNISEdit 脚本编辑器向导产生

; 安装程序初始定义常量
!define PRODUCT_NAME "Ethinkface"
!define PRODUCT_VERSION "0.1"
!define PRODUCT_PUBLISHER "Ethink, Inc."
!define PRODUCT_WEB_SITE "http://www.ethink.com"

SetCompressor lzma

; ------ MUI 现代界面定义 (1.67 版本以上兼容) ------
!include "MUI.nsh"

; MUI 预定义常量
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"

; 欢迎页面
!insertmacro MUI_PAGE_WELCOME
; 许可协议页面
;!insertmacro MUI_PAGE_LICENSE "..\..\..\path\to\licence\YourSoftwareLicence.txt"
; 安装目录选择页面
!insertmacro MUI_PAGE_DIRECTORY
; 安装过程页面
!insertmacro MUI_PAGE_INSTFILES
; 安装完成页面
!insertmacro MUI_PAGE_FINISH

; 安装界面包含的语言设置
!insertmacro MUI_LANGUAGE "SimpChinese"

; 安装预释放文件
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
; ------ MUI 现代界面定义结束 ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile ".\target\Ethinkface.exe"
InstallDir "D:\Ethinkface"
ShowInstDetails show
BrandingText " "

Section "MainSection" SEC01
  SetOutPath "$INSTDIR"
  SetOverwrite ifnewer
  File /r ".\target\Ethinkface\*.*"
SectionEnd

Section -Post
SectionEnd
