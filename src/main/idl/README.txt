Problème pour générer les classes java à partir des idl. Il n'est pas possible de modifier les packages (sens java)
des fichiers java générés. 
=> Les fichiers sont générer dans le repertoire courant. 
les 2 solution testés:
	- Avec le plugin maven idlj-maven-plugin: version limité de IDLJ. Développement stopper et la partie pkgPrefix 
	n'a jamais été implémenter
	- idlj: outil de la JVM. Permet de spécifier un package prefix mais apparement il ne doit pas être null à la base
	(i.e aucun module, au sens idl, est déclaré)
	- Spécifier l'arborescence dans les fichiers .idl grâce à module{}. Mais cela modifie le code généré notammenent 
	l'identification des objets dans Corba (ID Object).
	
Solution provisoire: le code généré est versionné. Sa regénération nécessite du bidouillage ( move + package rename)