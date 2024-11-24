# Projeto Telas para Smartwatch e Smartphone

Este projeto é um sistema de gerenciamento para restaurante, desenvolvido com foco em integração entre um aplicativo de smartwatch para garçons e um aplicativo de smartphone para os clientes.

## Funcionalidades principais
- **Cliente (Smartphone)**:
  - Reservar mesas.
  - Visualizar e editar reservas.
  - Navegar pelo cardápio.

- **Garçom (Smartwatch)**:
  - Selecionar itens do cardápio (pratos, bebidas e sobremesas).
  - Registrar pedidos.
  - Enviar pedidos diretamente para a cozinha.

---

## Pré-requisitos
Antes de rodar o projeto, você precisará de:
- [**Android Studio**](https://developer.android.com/studio): Baixe a versão mais recente.
- **Java Development Kit (JDK)**: Versão 11 ou superior.
- **SDK do Android 13 (API level 33 - Tiramisu)**.

---

## Configurando o projeto
1. **Clone o repositório**:
   Use o Git para clonar o projeto no seu computador:
   ```bash
   git clone https://github.com/LuccaCazarineDataShelf/ProjetoPucMobile.git

Abra o projeto no Android Studio:

- No Android Studio, vá em **File > Open**.
- Selecione a pasta onde o projeto foi salvo.
- **Sincronize o Gradle**: Assim que o projeto for carregado, clique em **Sync Now** no banner superior.

Configure o SDK:

- Vá para **File > Project Structure > SDK Location**.
- Confirme que o SDK está configurado para o Android 13 (API level 33 - Tiramisu).

Configure os emuladores (opcional): Configure os dispositivos virtuais no Android Studio:
- **Wear OS (Garçom)**: Tela de 454x454 px (API level 33).
- **Smartphone (Cliente)**: Use um dispositivo padrão como Pixel 4, API level 33.

---

## Alternando entre os módulos
Este projeto está dividido em dois módulos principais:
- **app (Cliente)**: Tela do cliente no smartphone.
- **wear (Garçom)**: Tela do garçom no smartwatch.

### Como alternar entre os módulos:
- No Android Studio, clique no menu suspenso ao lado do botão de **Run** (ícone de Play).
- Escolha o módulo que deseja executar:
  - **app** para testar a tela do cliente.
  - **wear** para testar a tela do garçom.

---

## Rodando o projeto
1. Escolha o módulo desejado (Cliente ou Garçom) como descrito acima.
2. Selecione o dispositivo para teste:
   - **Smartwatch** ou **emulador Wear OS** para o módulo do garçom.
   - **Smartphone** ou **emulador Android** para o módulo do cliente.
3. Clique no botão **Run** (ícone de Play) ou pressione **Shift + F10** no teclado.
   O aplicativo será compilado e instalado no dispositivo selecionado.

---

## Notas importantes

### Banco de Dados
O projeto utiliza **SQLite** para armazenar dados, como:
- Pedidos registrados pelo garçom.
- Reservas feitas pelos clientes.
- Usuários cadastrados.

Caso algo não apareça como esperado, verifique os dados diretamente no banco.

### Permissões
O módulo **Wear OS (Garçom)** pode solicitar permissões adicionais, como:
- **Internet**: Para comunicação futura com a cozinha.
- **Bluetooth**: Para sincronizar dados entre dispositivos.

Certifique-se de aceitar as permissões ao rodar o app.
