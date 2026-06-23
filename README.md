# Trabalho Prático 2: Desenvolvimento Mobile

**Descrição:** Aplicativo para registro e gerenciamento de chamados, desenvolvido com foco na persistência de dados utilizando o banco de dados nativo do Android.

> **📌 Nota para Avaliação:** O aplicativo é composto por **6 telas**. Para facilitar a correção e o acompanhamento do fluxo, foi adicionado um número ao título de cada tela temporariamente. Estes identificadores numéricos serão removidos em versões futuras.

---

## 🛠️ Processo Criativo e de Desenvolvimento

O fluxo de trabalho deste projeto uniu os conceitos vistos em aula com o uso estratégico de ferramentas de Inteligência Artificial para planejamento, prototipagem visual e apoio à codificação:

* **1. Planejamento (NotebookLM):** O processo começou com a inserção das referências do *Material Design 3 (Google)* e dos critérios de avaliação da disciplina na ferramenta. A partir disso, foi gerado o Documento de Requisitos do Produto (**PRD**) que guiou todo o escopo.
* **2. Prototipagem Visual (Gemini Pro):** Com o PRD em mãos, o Gemini foi utilizado (via modo Canvas) para gerar protótipos funcionais das telas em HTML/CSS. Essas telas serviram como referência visual e de usabilidade para o desenvolvimento nativo.
* **3. Interface (Android Studio):** A construção do layout (XML/Compose) foi feita buscando a maior fidelidade possível com os protótipos em HTML. Além disso, os IDs de todos os elementos interativos e botões essenciais foram nomeados estritamente conforme mapeado no PRD.
* **4. Lógica e Persistência (Claude):** A programação foi apoiada pelo Claude, que recebeu o PRD completo, a lista de IDs, as anotações feitas em aula e os padrões de projeto exigidos. Técnicas de gerenciamento de contexto foram aplicadas nos prompts para manter a coesão do código e evitar alucinações.
* **5. Entrega Final:** Após a validação das funcionalidades locais, o código finalizado recebeu o *push* no Git. A versão atual do repositório é exatamente a que se encontra instalada no dispositivo físico para a avaliação.
