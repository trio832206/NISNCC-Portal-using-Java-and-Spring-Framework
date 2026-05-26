UPDATE users SET password='$2a$10$Jj3NS2jrm9p35P8xOjHWzewN.g2lpZU6nNrFNM4O9E3RVvxu9HHxm' WHERE username='admin';
UPDATE users SET password='$2a$10$dPjhPekle6AsE4si6k5/V.vQdze7ZMIzVRFGHyADr880kqrTkx5RK' WHERE username='officer1';
UPDATE users SET password='$2a$10$n96Ov.eWrm9O4n7qJPj5BupnqStPzFwNTi7/krU4emqoQhbsF19s6' WHERE username IN ('cadet1','cadet2','cadet3');
SELECT username, LEFT(password,30) as pwd_preview FROM users;
