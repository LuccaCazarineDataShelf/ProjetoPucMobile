# Como executar o projeto

## 1. Pré-requisitos

- Android Studio instalado (versão recente).
- JDK já vem com o Android Studio.
- Conta no Firebase.

---

## 2. Clonar o repositório

```bash
git clone <URL-DO-REPOSITORIO>
cd ProjetoPucMobile
```

Abra a pasta **`ProjetoPucMobile`** no Android Studio (`File > Open`).

---

## 3. Configurar o Firebase (obrigatório para o app funcionar)

1. Acesse o [Firebase Console](https://console.firebase.google.com/) e crie um projeto.
2. Ative:
   - **Firestore Database**
   - **Authentication → Sign-in anônimo**
3. Registre **3 apps Android** no mesmo projeto Firebase, com estes IDs:

   - Módulo `app`  
     `applicationId`: `com.example.projetosandra`
   - Módulo `cozinha`  
     `applicationId`: `com.example.cozinha`
   - Módulo `telasmartwatch`  
     `applicationId`: `com.example.telasmartwatch`

4. Para cada app registrado no Firebase:
   - Baixe o arquivo `google-services.json`.
   - Salve nos caminhos:

   ```text
   app/google-services.json
   cozinha/google-services.json
   telasmartwatch/google-services.json
   ```

5. No Android Studio, rode **Sync Gradle**.

---

## 4. Rodar cada módulo

> Dica: se o PC for fraco, rode **apenas um emulador por vez**.

### 4.1. Módulo `app` (tablet/cliente)

1. Crie um emulador Android normal (Phone/Tablet).
2. No topo do Android Studio, selecione a configuração de execução do módulo **`app`**.
3. Clique em **Run ▶**.

### 4.2. Módulo `cozinha` (tablet/cozinha)

1. Crie (ou reutilize) um emulador Android.
2. Selecione a configuração de execução do módulo **`cozinha`**.
3. Clique em **Run ▶**.

### 4.3. Módulo `telasmartwatch` (Wear OS/garçom)

1. Crie um emulador **Wear OS** no Android Studio.
2. Selecione a configuração de execução do módulo **`telasmartwatch`**.
3. Clique em **Run ▶**.
4. No primeiro uso, aceite a permissão de **notificações** no relógio.

---

## 5. Observações rápidas

- Se aparecer erro de índice do Firestore no Logcat, abra o link sugerido pelo Firebase e crie o índice (basta aceitar as opções padrão).
- Se algum módulo não encontrar o Firebase, verifique se:
  - O `google-services.json` está no módulo correto.
  - O `applicationId` no `build.gradle` corresponde ao cadastrado no Firebase.
