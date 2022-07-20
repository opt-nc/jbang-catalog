# Le catalogue JBang de OPT-NC


Ici seront référencés des démos, mini projets, exécutables très facilement grâce à (J)Bang.

## Usage

### 👉 Prérequis

- Avoir [`sdkman`](https://sdkman.io/) installé
- Installer [JBang!](https://www.jbang.dev/documentation/guide/latest/installation.html) :
```shelll
sdk install jbang
```


## 🚀 Lancer les scripts avec (J)Bang!

Il existe plusieurs façons de lancer les scripts référencés dans le catalog. 
Exemple de `hello-optnc` :

En créant un alias local au poste de dev :
```shell
jbang alias add --name hello-optnc https://github.com/opt-nc/jbang-catalog/blob/main/hello-optnc/HelloOptNc.java
# Check des alias
jbang alias list
# Appel du script
jbang hello-optnc
```

ou directement
```shell
jbang  hello-optnc@opt-nc/jbang-catalog
```

Enfin, pour rester à jour des ajouts de la communauté, mettre à jour le cache :

```shell
jbang cache clear
jbang  hello-optnc@opt-nc/jbang-catalog
```

