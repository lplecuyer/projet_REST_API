# TP2

## Rétrospective
### Métriques

- Le temps pour implémenter une feature varie entre 1 et 3 jours, pour une moyenne de 2 jours.
- Le temps pour intégrer une pull request varie entre 1 heure et 7 jours, pour une moyenne de 56.67 heures.
- Le nombre de personnes sur chaque feature varie entre 1 et 2 personnes, pour une moyenne de 2 personnes.
- Le nombre de personnes qui évaluent une pull request est dans tous les cas 1.
- Le nombre d'issues en cours d'implémentation varie entre 0 et 4 issues pour une moyenne de 3.
- Le nombre de pull request en cours de review en même temps varie entre 1 et 2, pour une moyenne de 1.

### Réflexions

1. Le temps d'implémentation de chaque fonctionnalité semble approprié, mais le début de leur implémentation est un peu trop proche de la date de remise. Pour les pull requests, 2 jours pour review semble assez pour laisser le temps à l'équipe d'évaluer le code; 1 heure est bien trop trop court et une semaine est bien trop long.
2. Le temps d'implémentation ou de l'intégration nécessaire pour une fonctionnalité varie grossièrement comme une fonction quadratique selon la taille de l'issue ou de la pull request : un issue trop court (peu explicatif) demandera plus de temps à effectuer, car les spécifications ne seront pas claires; en revanche un issue trop court créera une complexité non nécessaire, augmentant aussi le temps d'implémentation. La concision et la clarté sont de mise.
3. Afin d'améliorer notre processus : 
    1) Le temps de review pour chaque pull request devrait être un peu plus long, et le nombre d'évaluateurs devrait idéalement être de 2 afin d'assurer une meilleure qualité du code. Cependant, certaines ont pris beaucoup trop de temps à être intégrées. Alors, review une PR ne devrait pas trainer plus de 4 jours.
    2) Une dâte de fin préventive devrait être établie - finir l'implémentation de chaque issue idéalement 5 jours avant la remise afin d'avoir le temps de review, puis intégrer et d'effectuer les correctifs. La création d'un diagramme de Gantt pourrait aider.
    3) Une meilleure clarification lors de l'attribution des tâches serait la bienvenue. Un survol de chacune des tâches afin de s'entendre sur la manière d'implémenter ou de structurer les différentes composantes permettrait de diminuer la modification de code après-coup.