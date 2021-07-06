@ECHO OFF

del %2\%1.boh
del %2\%1.post.res
del %2\%1.post.dat
del %2\%1.err

rem OutputFile: %2\%1.boh
rem ErrorFile: %2\%1.err

%3\vigapt.exe %2\%1
