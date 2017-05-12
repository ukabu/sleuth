### ressources
Ce pipeline utilise deux ressources:
- https://github.com/ukabu/sleuth.git
- deploy-cf

### paramètres
Les paramètres pour `deploy-cf`doivent être inclus lors de commade `set-pipeline`. Vous pouvez copier le fichier `private.yml.example` vers `private.yml` (ce dernier est ignoré pour Git). Après avoir modifier le fichier pour spécifier les paramètres, lancer :
```
fly -t local set-pipeline -p sleuth -c pipeline.yml -l private.yml
```

Vous devez spécfier tous les paramètres préfixés de `pcf-`. Les paramètres préfixés par `maven-` sont optionnels.

- `maven-opts` : options a àjouter à la ligne de commande `mvn`
- `maven-config-file`: fichier `settings.xml` à utiliser pour configurer Maven. le chemin de fichier est relatif au context d'exécution des tâches, donc, pour utiliser [`mirror-192.168.100.1-settings.xml`](mirror-192.168.100.1-settings.xml), il faut mettre la valeur `sleuth/ci/mirror-192.168.100.1-settings.xml`. Cet exemple permet d'utiliser un repomirror-192.168.100.1-settings.xml maven sité à l'adresse `192.168.100.1`.

### jobs
Le pipeline est constitué de 3 jobs:
- test : lance les tests unitaires des modules (à l'exception de `smoketests`).
- deploy : package les modules et lance le déploiement sur PCF en utilisant le [manifest](../manifest.yml).
- smoketests : compile et lance les test dans le module [smoketests](../smoketests/). Lorsque les tests sont terminés, les applications sont arrêtées.
