# Projeto de TCC de Gestão automatizada de salão de restaurantes


Como rodar o projeto
1. O que você vai precisar
Antes de começar, certifique-se de ter as ferramentas necessárias instaladas:

Android Studio: Você pode baixar a versão mais recente no site oficial.
JDK (Java Development Kit): A versão 11 ou superior já funciona bem.
SDK do Android 13 (API level 33 - Tiramisu): É essencial para rodar o projeto no emulador ou em dispositivos reais.
2. Configurando o projeto
Clone o projeto:

Se estiver usando o Git, basta rodar este comando:
bash
Copiar código
git clone https://github.com/SeuUsuario/SeuProjeto.git
Ou, se preferir, faça o download direto pelo GitHub.
Abra no Android Studio:

Depois de baixar, abra o Android Studio e clique em File > Open.
Selecione a pasta onde o projeto está salvo.
Sincronize o Gradle:

Assim que o projeto carregar, o Android Studio vai pedir para sincronizar o Gradle. É só clicar em Sync Now no topo da tela.
Configure o SDK:

Vá em File > Project Structure > SDK Location e confirme que o SDK está configurado para a versão Tiramisu (API level 33).
Configurar os emuladores (opcional):

Para testar o app, você pode configurar emuladores:
Wear OS (para o smartwatch): Tela de 454x454 px, API level 33.
Smartphone (para o cliente): Use um dispositivo padrão, como Pixel 4, API level 33.
3. Escolhendo entre a tela do smartwatch e a do celular
O projeto está dividido em dois módulos:

app (Cliente): Tela para o usuário no celular.
wear (Garçom): Tela para o garçom no smartwatch.
Para alternar entre eles:

No Android Studio, clique no menu suspenso ao lado do botão de Run (ícone de Play, no topo).
Escolha o módulo que você quer rodar:
app para testar a tela do cliente.
wear para testar a tela do garçom.