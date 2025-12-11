![ICEI](images/icei-pucminas.png)

# 🎶🔊 Promptify - Mini-player de Terminal
---
## Alunos integrantes da equipe

* [Alexandre Versiani Raposo](https://github.com/LexVRaps)
* [Guilherme Meireles Farias)](https://github.com/guimfarias)
* [Marcos Vinicius dos Santos Pereira](https://github.com/marcosvisa)
* [Nome completo do aluno 5 (com link para Github)](https://github.com/aluno5)

## Professor responsável

* Pedro Felipe Alves de Oliveira

## Gerenciamento do Projeto

Desenvolver e lançar no mercado uma plataforma de streaming musical em ambiente de linha de comando (CLI), voltada para usuários que buscam uma solução leve, sem interface gráfica, com foco em desempenho, praticidade e integração em ambientes técnicos.

Fases do Gerenciamento do Projeto:
1. [Iniciação](docs/01-iniciacao)
2. [Planejamento](docs/02-planejamento)
3. [Execução](docs/03-execucao)
4. [Monitoramento](docs/04-monitoramento)
5. [Encerramento](docs/05-encerramento)

## ⚙ Como Funciona 

O usuário, através do prompt de comando, digita a música/artista desejado e o algorítimo, através da ferramenta yt-dlp, extrai o audio do primeiro resuldado de busca e força o VLC a tocar.


Exemplo: ytsearch1:"musica"
Busca no YouTube como se fosse um usuário

Pega o primeiro resultado (ytsearch1:)

Extrai o link direto do áudio

Envia para o VLC tocar


## 💻 Requisitos para Executar Sistema 

* Java JDK 8 ou superior
* VLC Instaldo 
* Maven Instalado 
---

### ▶️ Executar Primeira Vez:

```powershell
.\executar.bat
```

### 📌 Compilar o projeto (Windows PowerShell):

```powershell
mvn clean compile
```

### ▶ Executar o sistema:

```powershell
mvn exec:java69
```
