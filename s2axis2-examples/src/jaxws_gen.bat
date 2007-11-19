@echo off

if "%JAVA_HOME_6%" == "" (
    echo.
    echo Java SE 6.x ��HOME���w�肳��Ă��܂���B
    echo ���ϐ� JAVA_HOME_6 ���w�肵�Ă��������B
    echo.
    
    pause
    exit /b
)


@rem ���ϐ��̐ݒ�
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


@rem �r���h�̊J�n
@rem ---------------------------------------------------------------------------
echo.
echo JAX-WS�ɂ��T�[�r�X�̃r���h���J�n���܂��B

@rem �T�[�o�T�C�h�̃r���h
@rem ---------------------------------------------------------------------------
echo.
echo �T�[�o�T�C�h�̃r���h
echo.

%JAVA_HOME_6%\bin\wsgen -d %SERVER_CLASS_PATH% -r %SERVER_DEST_PATH% -s %SERVER_SRC_PATH% -cp %SERVER_CLASS_PATH% -wsdl %PACKAGE%.%SERIVCE_CLASS%

set WSDL_FILE=%SERVER_DEST_PATH%\%SERIVCE_NAME%.wsdl
if exist %WSDL_FILE% (
    echo Generated WSDL           :
    echo     %WSDL_FILE%
) else (
    echo Error! WSDL�t�@�C���̐����Ɏ��s���܂����B
    exit /b
)

set SERVER_SRC_FILE=%SERVER_SRC_PATH%\%PACKAGE_PATH%\jaxws
if exist %SERVER_SRC_FILE% (
    echo Generated server sources : %SERVER_SRC_FILE%
    for /f %%V in ('dir /b /s %SERVER_SRC_FILE%') do (
        echo     %%~nV%%~xV
    )
) else (
    echo Error! �T�[�o�[�̃\�[�X�t�@�C���̐����Ɏ��s���܂����B
    exit /b
)

set SERVER_CLASS_FILE=%SERVER_CLASS_PATH%\%PACKAGE_PATH%\jaxws
if exist %SERVER_CLASS_FILE% (
    echo Generated server classes : %SERVER_CLASS_FILE%
    for /f %%V in ('dir /b /s %SERVER_CLASS_FILE%') do (
        echo     %%~nV%%~xV
    )
) else (
    echo Error! �T�[�o�[�̃\�[�X�t�@�C���̃r���h�Ɏ��s���܂����B
    exit /b
)


@rem �N���C�A���g�T�C�h�̃r���h
@rem ---------------------------------------------------------------------------
echo.
echo �N���C�A���g�T�C�h�̃r���h
echo.

%JAVA_HOME_6%\bin\wsimport -p %PACKAGE%.client -d %CLIENT_CLASS_PATH% -s %CLIENT_SRC_PATH% -wsdllocation %WSDL_URI%/%SERIVCE_NAME%.%SERVICE_CLASS%Port?wsdl %SERVER_DEST_PATH%\%SERIVCE_NAME%.wsdl

set CLIENT_SRC_FILE=%CLIENT_SRC_PATH%\%PACKAGE_PATH%\client
if exist %CLIENT_SRC_FILE% (
    echo Generated client sources : %CLIENT_SRC_FILE%
    for /f %%V in ('dir /b /s %CLIENT_SRC_FILE%') do (
        echo     %%~nV%%~xV
    )
) else (
    echo Error! �N���C�A���g�̃\�[�X�t�@�C���̐����Ɏ��s���܂����B
    exit /b
)

set CLIENT_CLASS_FILE=%CLIENT_CLASS_PATH%\%PACKAGE_PATH%\client
if exist %CLIENT_CLASS_FILE% (
    echo Generated client classes : %CLIENT_CLASS_FILE%
    for /f %%V in ('dir /b /s %CLIENT_CLASS_FILE%') do (
        echo     %%~nV%%~xV
    )
) else (
    echo Error! �N���C�A���g�̃\�[�X�t�@�C���̃r���h�Ɏ��s���܂����B
    exit /b
)

