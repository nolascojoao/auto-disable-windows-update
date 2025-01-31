### O que é?


O serviço **Windows Update** no **Windows 10** pode ser bastante incômodo, já que desabilitá-lo de forma permanente é praticamente impossível. Mesmo com alterações no registro, ele eventualmente é reativado automaticamente. 


Este script simples verifica a cada 15 minutos se o serviço está ativo e, caso esteja, desativa e interrompe sua execução. O processo leva menos de 3 segundos para ser concluído.


>[!TIP]
>Você pode alterar o intervalo de execução do script na linha 16 da classe MonitorWindowsUpdate.java, antes de compilar o arquivo.
>```bash
>private static final int INTERVAL_MINUTES = 15;
>```

---

### Como usar?

Siga os passos abaixo para compilar e gerar um executável para o seu script Java no Eclipse:

**1. Exportar o Projeto como JAR**

  - No Eclipse, clique com o botão direito no projeto.

  - Vá em Export > Runnable JAR file.

  - Configure as opções:
    - Launch Configuration: selecione a classe principal (que contém o main).
    - Export destination: escolha o caminho e nome do arquivo, como MonitorWindowsUpdate.jar.
    - Library handling: escolha Package required libraries into generated JAR.


    - Clique em Finish.


**2. Criar Arquivo BAT para Execução**

O Windows não permite executar diretamente arquivos .jar ao clicar, mas você pode criar um arquivo BAT para facilitar:

  - Abra o Bloco de Notas e insira o seguinte comando:

```bash
java -jar "C:\caminho_do_seu_jar\MonitorWindowsUpdate.jar"
```

  - Salve o arquivo como `executar.bat`.


>[!NOTE]
>O script pode ser executado diretamente no console do Eclipse, sem a necessidade de compilação.


**3. Testar o Arquivo BAT**

Dê um duplo clique no arquivo executar.bat para rodar a aplicação.


>[!IMPORTANT]
>É necessário conceder permissões de administrador para que o script consiga parar o serviço.

---

![wps](https://github.com/user-attachments/assets/36b03fdf-1e0b-420a-9dd7-e49325f60324)

![running](https://github.com/user-attachments/assets/ceffd68b-2e4e-4701-936e-81ac56ba0df7)

![wps2](https://github.com/user-attachments/assets/374a8127-fede-401e-a9c9-a647954d60e5)
