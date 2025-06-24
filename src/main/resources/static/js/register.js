document.addEventListener('DOMContentLoaded', function () {
    const submitButton = document.getElementById('submit');

    submitButton.addEventListener('click', () => {
        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('passwordcon').value;

        if (password === confirmPassword) {
            const data = { username, email, password };

            fetch('/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.ok) {
                        alert('Registracija uspješna!');
                        window.location.href = '/login';
                    } else {
                        alert('Registracija nije uspjela.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Došlo je do greške.');
                });
        } else {
            alert('Lozinke se ne podudaraju!');
        }
    });
});
