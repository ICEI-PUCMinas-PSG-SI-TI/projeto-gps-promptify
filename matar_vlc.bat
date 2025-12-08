@echo off
chcp 65001 > nul
echo 🛑 Parando PROMPTFY...
taskkill /F /IM vlc.exe >nul 2>&1
taskkill /F /IM yt-dlp.exe >nul 2>&1
echo > Todos processos encerrados
echo.
echo Dica: Execute .\executar.bat para reiniciar
pause