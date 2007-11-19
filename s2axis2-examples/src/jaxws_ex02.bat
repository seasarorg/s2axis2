@echo off

set PACKAGE=org.seasar.remoting.axis2.examples.jaxws.ex02
set SERVICE_CLASS=CalculatorService
set SERVICE_NAME=CalcService
set WSDL_LOCATION=http://localhost:8080/s2axis2-examples/services

call jaxws_gen.bat %PACKAGE% %SERVICE_CLASS% %SERVICE_NAME% %WSDL_LOCATION%

echo.
echo.
pause


