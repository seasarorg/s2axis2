@echo off

if "%JAVA_HOME_6%" == "" (
    echo.
    echo Java SE 6.x のHOMEが指定されていません。
    echo 環境変数 JAVA_HOME_6 を指定してください。
    echo.
    
    pause
    exit /b
)


@rem 環境変数の設定
@rem ---------------------------------------------------------------------------

set PACKAGE=%1
set SERIVCE_CLASS=%2
set SERIVCE_NAME=%3
set WSDL_URI=%4

set PACKAGE_PATH=%PACKAGE:.=\%

set SERVER_SRC_PATH=main\java
set SERVER_DEST_PATH=main\java\%PACKAGE_PATH%
set SERVER_CLASS_PATH=main\webapp\WEB-INF\classes

set CLIENT_SRC_PATH=test\java
set CLIENT_CLASS_PATH=..\target\test-classes


@rem ビルドの開始
@rem ---------------------------------------------------------------------------
echo.
echo JAX-WSによるサービスのビルドを開始します。

@rem サーバサイドのビルド
@rem ---------------------------------------------------------------------------
echo.
echo サーバサイドのビルド
echo.

%JAVA_HOME_6%\bin\wsgen -d %SERVER_CLASS_PATH% -r %SERVER_DEST_PATH% -s %SERVER_SRC_PATH% -cp %SERVER_CLASS_PATH% -wsdl %PACKAGE%.%SERIVCE_CLASS%

set WSDL_FILE=%SERVER_DEST_PATH%\%SERIVCE_NAME%.wsdl
if exist %WSDL_FILE% (
    echo Generated WSDL           :
    echo     %WSDL_FILE%
) else (
    echo Error! WSDLファイルの生成に失敗しました。
    exit /b
)

set SERVER_SRC_FILE=%SERVER_SRC_PATH%\%PACKAGE_PATH%\jaxws
if exist %SERVER_SRC_FILE% (
    echo Generated server sources : %SERVER_SRC_FILE%
    for /f %%V in ('dir /b /s %SERVER_SRC_FILE%') do (
        echo     %%~nV%%~xV
    )
) else (
    echo Error! サーバーのソースファイルの生成に失敗しました。
    exit /b
)

set SERVER_CLASS_FILE=%SERVER_CLASS_PATH%\%PACKAGE_PATH%\jaxws
if exist %SERVER_CLASS_FILE% (
    echo Generated server classes : %SERVER_CLASS_FILE%
    for /f %%V in ('dir /b /s %SERVER_CLASS_FILE%') do (
        echo     %%~nV%%~xV
    )
) else (
    echo Error! サーバーのソースファイルのビルドに失敗しました。
    exit /b
)


@rem クライアントサイドのビルド
@rem ---------------------------------------------------------------------------
echo.
echo クライアントサイドのビルド
echo.

%JAVA_HOME_6%\bin\wsimport -p %PACKAGE%.client -d %CLIENT_CLASS_PATH% -s %CLIENT_SRC_PATH% -wsdllocation %WSDL_URI%/%SERIVCE_NAME%.%SERVICE_CLASS%Port?wsdl %SERVER_DEST_PATH%\%SERIVCE_NAME%.wsdl

set CLIENT_SRC_FILE=%CLIENT_SRC_PATH%\%PACKAGE_PATH%\client
if exist %CLIENT_SRC_FILE% (
    echo Generated client sources : %CLIENT_SRC_FILE%
    for /f %%V in ('dir /b /s %CLIENT_SRC_FILE%') do (
        echo     %%~nV%%~xV
    )
) else (
    echo Error! クライアントのソースファイルの生成に失敗しました。
    exit /b
)

set CLIENT_CLASS_FILE=%CLIENT_CLASS_PATH%\%PACKAGE_PATH%\client
if exist %CLIENT_CLASS_FILE% (
    echo Generated client classes : %CLIENT_CLASS_FILE%
    for /f %%V in ('dir /b /s %CLIENT_CLASS_FILE%') do (
        echo     %%~nV%%~xV
    )
) else (
    echo Error! クライアントのソースファイルのビルドに失敗しました。
    exit /b
)

