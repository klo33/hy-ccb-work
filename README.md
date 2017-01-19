# CyberSecurity project report
Sources FLAWED version: https://github.com/klo33/hy-ccb-work/tree/master
Sources FIXED version: https://github.com/klo33/hy-ccb-work/tree/flaw-fixed

Nicer version on REPORT.txt-file

## FLAW 1: SQL-injection (second order) (A1)
### Issue: SQL Injection attact to gain all user account details
#### Steps to reproduce:
1. Go to address localhost:8080/events/10/form to signup to a Test event
2. Enter 
> Name as `Jaakko' OR 1=1;--`
> Email address as blahblah@blah.com
> Address as blahblah
and submit
3. After submit, a account for user is created and click link to get to own details
4. In own details a list of all accounts in system is shown.
#### Prevention: 
Use Spring Data Repository and Thymelead instead of direct database SQL-inquiry. See line 88 on sec.project.controller/UserController.java.

## FLAW 2: XSS allow Javascript execution (A3)
### Issue: Stored XSS vulnerability
#### Steps to reproduce:
1. Sign up to a Test event at address localhost:8080/events/10/form
2. Enter following details
> Name: Blah blah
> Email: blahblah2@blah.com
> Address: `<script>alert("It's running javascript");</script>`
and submit signup
3. Javascript entered in runned and pop-up alert appears (in vanilla Firefox).
4. Signout
5. Enter address localhost:8080 and sign-in as administrator username: al, password al.
6. Click link "Events" and select "Test event" -link for details.
7. Javascript entered earlier is runned and pop-up alert appears (tested in latest vanilla Firefox)
#### Preventions: 
Never user Thymeleaf th:utext on unsafe Strings, use escaped `th:text` instead. See src/main/resources/templates/done.html and event.html.

## FLAW 3A:  Missing function level access control (A7)
### Issue: Missing function level access control
Allow access to hidden events and access AND access to user manegement even if not admin.
#### Steps to reproduce:
1. Sign in as normal user, usename: ted, password: ted
2. Click Events-link to get event-list. There is no Secret meeting present.
3. Open page HTML source
4. In the source, there is a list of events, in which to Secret meeting appear only with tag class="hidden", which marks it with display: none CSS.
5. Enter the address from the html-source to address bar ad you'ra able to enter event-details.
#### Prevention:
Filter to passed on list on program logic. See sec.project.controller / EventController,java line 39 onward.

## AND
## FLAW 3A:  Missing function level access control (A7)
### Issue: Site missing function level access control (OWASP A7).
#### Steps to reproduce:
1. Signup to a public event Test event in address localhost:8080/events/10/form
2. Enter signup details as
> Name: blahblah
> Email: blah3@blah.com
> Address: blah
and submit.
3. Take note of the password as you need it for further login.
4. Logout blahblah
5. Go to address localhost:8080. Login as administrator account "al", password "al"
6. Start BurpSuite or similar packet proxy
Proxy > Edit Proxy setting 127.0.0.1:8090 and edit browser proxy to 127.0.0.1:8090
7. Click Users in your browser
8. Start packet intercept in BurpSuite
9. Add blah3@blah.com `ADMIN` right (select `ADMIN` and select Add)
10. Send intercepted packet to Repeater in BurpSuite
11. Turn off the packet intercept in BurpSuite
12. Select Log out for Al in browser(administrator)
13. Login with blah3@blah.com with password given earlier
14. Turn on packet interceptor in BurpSuite
15. Click own details
16. In BurpSuite copy `JSESSIONID=` value to clipboard from intercepted packet and Drop packet
17. Turn off packet interceptor
18. Paste `JSESSIONID=` to Repeater window packet, select "Pencil" icon for target and set localhost 8080, select Go
19. In browser select Log out for blah3@blah.com
20. Login blah3@blah.com again. 
21. Select Users. You have gained a ADMIN access.
Any user can promote their rights by sending POST, with `_method="PUT"` and `auth="ADMIN"` to `/users/{id}/promote`
#### Prevention: 
Inquire authority beforehand on critical tasks. See e.g. sec.project.config / SecurityConfiguration.java line 38.

## FLAW 4: CSRF Cross site request forgery - allow password change (A8)
### Issue: Cross site request forgery - Site allows password change with get request
#### Steps to reproduce:
1. Enter in private browser window e.g. address localhost:8080/events/10/form
2. Enter following signup details
> Name: blahblah
> Email: blah4@blah.com
> Address: `<img src="/users/own/password?pw1=none&pw2=none"/>`
and submit.
3. Enter address localhost:8080 to your browser and log in as "al" password "al".
4. Select Events from navigation
5. Select from eventlist Test event
6. Logout and try to log in with "al" and password "al". You will not succeed as the password is now "none"
#### Prevention: Reauthenticate user before password change. See sec.project.controller / UserController.java line 100.

## FLAW 5. Allow redirect to arbitrary address from `/redirect?to=`
### Issue: Unvalidated redirect (A10)
1. Write to browsers address `localhost:8080/redirect?to=cs.helsinki.fi`
You will be redirected to the arbitrary address (cs.helsinki.fi)
#### Prevention: 
Deny access to redirect. See e.g. sec.project.config / SecurityConfiguration.java line 34
