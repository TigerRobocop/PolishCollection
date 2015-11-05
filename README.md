# PolishCollection
Android final project - Unibratec

# Avaliação oral - basicamente descreve boa parte do que a aplicação é capaz de fazer
Vou tentar listar as perguntas feitas na minha apresentação de projeto Android:
-Várias perguntas à respeito da diferenciação de layout default/tablet, por exemplo: em que pasta define-se o layout alternativo (res/layout-large), como fazer essa verificação em tempo de execução e exibir o layout correto (criar um arquivo bools.xml na pasta res/values e res/values-large, com os devidos valores, e fazer esta verificação na Main Activity, no método click do item da lista).
-Onde e como, em código, se troca uma view no layout por um fragment em tempo de execução (no nosso projeto, foi na Main Activity se for tablet, e na Details Activity se não for tablet)
-Onde e como fizemos a requisição ao webservice para pegar informação da internet (Numa classe AsyncTask dentro do ListFragment, no método doInBackground), e se usamos e/ ou quais bibliotecas usamos para auxiliar esse processo (GSON e OkHttp)
-Onde importamos estas bibliotecas (build.gradle)
-Se estas bibliotecas requerem alguma permissão específica (no manifest.xml adiciona-se permissão de internet, e tbm devemos adicionar uma permissão de network para fazer aquela verificação de conexão antes de iniciar a thread que faz esse processo)
-Como implementar o TabLayout e ViewPager na Main Activity (facinho, néam)
-Acesso ao banco: qual classe criamos para abrir o bco SQLite (um DBHelper da vida, que extends SQLiteOpenHelper, com os devidos métodos implementados, onCreate e onUpdate. Explicar o que eles fazem.
-Uma classe de métodos básicos de acesso ao bco local, no nosso caso criamos métodos Insert(object), Delete(object), GetAll() que retorna a lista de objetos,  e IsFavorite(object) numa classe DAO. Explicar como estes métodos funcionam.
-Explicar onde e pra quê usamos estes métodos(No click do menu item da Details Fragment, verificamos se este item é favorito, se sim, delete, se não, insert. E no método Load List do fragment da lista do bco local)
-Ele não me perguntou sobre o padrão EventBus, mas viu funcionando (usamos ele para atualizar a lista do bco local), com auxílio da biblioteca Otto, ele poderá perguntar a vocês.
-Ele fez mais algumas perguntas referentes às funcionalidades adicionais que eu implementei no meu projeto. Não relevante.

Então.. Acho que é só (!!!!!) isso, hehe :)
