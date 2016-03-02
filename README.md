# patch-mule-transport-http
Patch för Mule-3.7.0 HTTP-transport för att i HTTP-response kunna hantera flera samtidiga "Set-Cookie" headers med olika kombinationer av gemener/versaler.
Resulterar i ett exception i Mule utan patch.

Se även:

1. https://skl-tp.atlassian.net/browse/SKLTP-792
2. https://www.mulesoft.org/jira/browse/MULE-7680

## Deployment av patch
1. Ladda ned patch från: http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22se.skltp.patch.mule.transport.http%22%20AND%20a%3A%22skltp-patch-mule-transport-http%22
2. Lägg skltp-patch-mule-transport-http-VERSION.jar i katalogen: mule-standalone-3.7.0/lib/user

## Testning
Kräver en webserver/proxy som tillåter att HTTP-headers sätts med olika kombinationer av gemener/versaler.
Se JUnit-tester för detaljer.