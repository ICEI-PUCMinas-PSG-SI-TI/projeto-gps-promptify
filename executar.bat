@echo off
chcp 65001 > nul
title PROMPTFY v1.0

echo ========================================
echo            PROMPTFY - v1.0
echo ========================================
echo.

echo 🔧 Limpando processos e arquivos antigos...
call :limpar_tudo

echo > Verificando Java e Maven...
where java >nul 2>&1 || (echo ❌ Java não encontrado! & pause & exit /b 1)
where mvn >nul 2>&1 || (echo ❌ Maven não encontrado! & pause & exit /b 1)

echo > Compilando...
call mvn clean compile -q

if errorlevel 1 (
    echo > Erro na compilação!
    echo.
    echo 💡 Verifique:
    echo   1. java -version
    echo   2. mvn -version
    echo   3. Execute como Admin se necessário
    pause
    exit /b 1
)

echo > Compilado com sucesso!
echo > Iniciando PROMPTFY...
echo ========================================

mvn exec:java

:limpeza
echo.
call :limpar_tudo
echo Pressione qualquer tecla para sair...
pause >nul
exit /b

:limpar_tudo
REM 
taskkill /F /IM vlc.exe >nul 2>&1
taskkill /F /IM yt-dlp.exe >nul 2>&1


del compilando.txt 2>nul
del compilado.txt 2>nul
del verificando.txt 2>nul
del *.log 2>nul
del output.txt 2>nul
del log.txt 2>nul

REM 
timeout /t 1 /nobreak >nul
exit /b