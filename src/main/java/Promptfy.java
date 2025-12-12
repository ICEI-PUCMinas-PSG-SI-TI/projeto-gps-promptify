/**
 * PROMPTFY - Terminal Music Player
 * Projeto elaborado durante a disciplina de Grêrencia de Projeto de Softwere
 * Grupo 4 - Engenharia de Computação, 2025 - Segundo Semestre
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Promptfy {
    
    private static Process vlcProcess = null;
    
    public static void main(String[] args) throws Exception {
        exibirBanner();
        verificarDependencias();
        criarScriptPlayback(); // APENAS play_music.bat necessário
        iniciarPlayer();
    }
    
    private static void exibirBanner() {
        System.out.println("=========================================");
        System.out.println("                 PROMPTFY");
        System.out.println("    Terminal Music Player");
        System.out.println("=========================================");
        System.out.println("   • Use controles do VLC para pausar/parar");
        System.out.println("   • Feche a janela do VLC para parar");
        System.out.println("=========================================\n");
    }
    
    private static void verificarDependencias() throws Exception {
        System.out.println("> Verificando dependências...");
        
        File vlc = new File("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe");
        if (!vlc.exists()) {
            throw new Exception("❌ VLC não encontrado!\n" +
                              "Instale: https://www.videolan.org/vlc/");
        }
        System.out.println("> VLC encontrado");
        
        File ytDlp = new File("yt-dlp.exe");
        if (!ytDlp.exists()) {
            System.out.println("> Baixando yt-dlp...");
            baixarYtDlp();
        }
        System.out.println("> yt-dlp pronto");
        System.out.println();
    }
    
    private static void baixarYtDlp() throws Exception {
        String url = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe";
        try (InputStream in = new URL(url).openStream();
             FileOutputStream out = new FileOutputStream("yt-dlp.exe")) {
             
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
    
    private static void criarScriptPlayback() throws Exception {
        
        File batFile = new File("play_music.bat");
        if (!batFile.exists()) {
            try (PrintWriter writer = new PrintWriter(batFile)) {
                writer.println("@echo off");
                writer.println("chcp 65001 > nul");
                writer.println("echo [PROMPTFY] Buscando: %1");
                writer.println("echo [PROMPTFY] VLC abrirá em janela separada");
                writer.println("yt-dlp.exe ytsearch1:\"%1\" -f bestaudio -o - --no-playlist --quiet ^");
                writer.println("  | \"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\" - ^");
                writer.println("  --qt-minimal-view ^");  //abre o vlc
                writer.println("  --play-and-exit ^");
                writer.println("  --no-video");
                writer.println("echo.");
                writer.println("echo [PROMPTFY] Reprodução finalizada pelo VLC.");
            }
            System.out.println("> Script play_music.bat criado");
        }
        
        // NÃO cria matar_vlc.bat (já existe manualmente)
        // NÃO cria ajuda.bat (help está no próprio Java)
    }
    
    private static void iniciarPlayer() throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        limparProcessosAntigos();
    
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🔧 Encerrando processos...");
            limparProcessosAntigos();
        }));
        
        while (true) {
            System.out.print("\n🎶 Digite música/artista (ou 'sair'): ");
            String query = scanner.nextLine().trim();
            
            if (query.equalsIgnoreCase("sair") || query.equalsIgnoreCase("q")) {
                System.out.println("\n👋 Encerrando PROMPTFY...");
                limparProcessosAntigos();
                break;
            }
            
            if (query.equalsIgnoreCase("s") || query.equalsIgnoreCase("parar")) {
                System.out.println("\n🛑 Parando VLC...");
                limparProcessosAntigos();
                continue;
            }
            
            if (query.equalsIgnoreCase("help") || query.equalsIgnoreCase("ajuda")) {
                exibirAjuda();
                continue;
            }
            
            if (query.isEmpty()) {
                System.out.println("⚠️  Digite algo!");
                continue;
            }
            
            // Para música atual antes de tocar nova
            limparProcessosAntigos();
            
            // Toca nova música
            tocarMusica(query);
            
            System.out.println("\n Aguarde o VLC iniciar...");
            System.out.println(" Controles na janela do VLC:");
            System.out.println("   ▸ Botão ▶ ⏸ para pausar");
            System.out.println("   ▸ Botão ⏹ para parar");
            System.out.println("   ▸ X para fechar janela");
            System.out.println("\n> Digite a proxima musica");
        }
        
        scanner.close();
    }
    
    private static void exibirAjuda() {
        System.out.println("\n.> AJUDA DO PROMPTFY:");
        System.out.println("=====================");
        System.out.println("🎵 Para TOCAR: Digite nome da música/artista");
        System.out.println("");
        System.out.println("Controles (na janela do VLC):");
        System.out.println("   • ▶ || = Play/Pause");
        System.out.println("   • ⏹ = Stop");
        System.out.println("   • >> = Volume");
        System.out.println("   • X = Fechar");
        System.out.println("");
        System.out.println("⌨> Comandos no Terminal:");
        System.out.println("   • Digite outra música = Toca próxima");
        System.out.println("   • 's' ou 'parar' = Para VLC");
        System.out.println("   • 'q' ou 'sair' = Sai do programa");
        System.out.println("");
        System.out.println("> Se precisar forçar parada:");
        System.out.println("   Execute: .\\matar_vlc.bat");
        System.out.println("   Ou: taskkill /F /IM vlc.exe");
        System.out.println("");
    }
    
    private static void tocarMusica(String query) {
        try {
        System.out.println("\n🔍 Buscando: \"" + query + "\"...");

        ProcessBuilder pb = new ProcessBuilder(
            "yt-dlp.exe", "ytsearch1:" + query, "-f", "bestaudio", "--get-url"
        );
        Process p = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String url = reader.readLine();
        p.waitFor();

        if (url == null || url.isEmpty()) {
            System.out.println("⚠️ Nenhum resultado encontrado!");
            return;
        }

        System.out.println("▶️ Iniciando VLC com: " + url);

        ProcessBuilder playPb = new ProcessBuilder(
            "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe",
            url,
            "--qt-minimal-view",
            "--play-and-exit",
            "--no-video",
            "--meta-title", "PROMPTFY: " + query
        );
        vlcProcess = playPb.start();
        new Thread(() -> {
            try {
                vlcProcess.waitFor();
                System.out.println("\n> VLC fechado. Pronto para próxima música.");
            } catch (InterruptedException e) {
            }
        }).start();
        }catch (Exception e) {
        System.err.println("\n> Erro: " + e.getMessage());
        System.out.println("> Verifique se o VLC está instalado e o caminho está correto.");
        }
    }
    
    private static void limparProcessosAntigos() {
        try {
            // Para processo atual se existir
            if (vlcProcess != null && vlcProcess.isAlive()) {
                vlcProcess.destroy();
                try {
                    vlcProcess.waitFor(1, java.util.concurrent.TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    vlcProcess.destroyForcibly();
                }
            }
            
            // Mata qualquer VLC que possa ter ficado
            Runtime.getRuntime().exec("taskkill /F /IM vlc.exe >nul 2>&1");
            Runtime.getRuntime().exec("taskkill /F /IM yt-dlp.exe >nul 2>&1");
            
            // Pequena pausa para garantir
            Thread.sleep(500);
            
        } catch (Exception e) {
            // Ignora erros na limpeza
        }
    }
}
