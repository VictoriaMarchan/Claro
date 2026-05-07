<h1 align="center">
   claro.
</h1>


##  Sobre o Projeto

O **claro.** é um aplicativo meteorológico  que une dados precisos a uma experiência de usuário imersiva. Construído com as tecnologias  de desenvolvimento Android. 

A arquitetura foi desenhada utilizando o padrão MVVM para garantir um fluxo de dados limpo entre o consumo da API e a interface de usuário, com telas componentizadas, gráficos customizados desenhados do zero e uma navegação intuitiva.



##  Principais Funcionalidades

* **UI Dinâmica e Emocional:** O design da tela principal e da tela de detalhes (incluindo os ícones geométricos) muda automaticamente dependendo da condição climática e do horário do dia fornecidos pela API.
* **Busca Inteligente com Autocomplete:** Filtro dinâmico na tela de pesquisa que atualiza os resultados instantaneamente à medida que o usuário digita.
* **Gráficos Nativos Customizados:** Gráfico do caminho do sol (Nascer/Pôr do sol) construído do zero utilizando a API de `Canvas` do Jetpack Compose para máxima fidelidade visual.
* **Arquitetura Reativa (`StateFlow`):** Gerenciamento de estado eficiente através do ViewModel, garantindo transições suaves entre carregamento, exibição de dados e tratamento de erros da API.
* **Navegação Componentizada:** Fluxo de telas seguro e isolado usando o `NavHost` do Compose.

##  Tecnologias Utilizadas

O projeto foi desenvolvido inteiramente no ecossistema moderno do Android:

* **[Kotlin]:** Linguagem de programação principal.
* **[Jetpack Compose]:** Toolkit moderno do Google para construção de UI nativa declarativa.
* **[Material Design 3]:** Componentes de design e suporte a temas (Scaffold, TopAppBar, Switches, etc).
* **Arquitetura MVVM:** Separação clara de responsabilidades entre lógica e interface.
* **StateFlow / Coroutines:** Gerenciamento assíncrono de requisições de rede.
* **[Retrofit]:** Cliente HTTP type-safe para consumo da API do OpenWeather.
* **Navegação (Navigation Compose):** Gerenciamento de rotas.

Faça o "Sync" do Gradle e clique em Run (Shift + F10).

1. Faça o clone do repositório:
```bash
git clone [https://github.com/SeuUsuario/claro-weather-app.git](https://github.com/SeuUsuario/claro-weather-app.git)
