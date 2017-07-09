const express = require('express');
const request = require('request');

const myApp = express();

myApp.get('/validate', function(req, resp) {
	const postData = {
		secret: 'your-secret-key',
		response: req.query.user_token
	};
	request.post({
		url: 'https://www.google.com/recaptcha/api/siteverify',
		form: postData
	}, function(error, response, body) {
		jsonData = JSON.parse(body);
		if(jsonData.success) {
			resp.send('PASS');
		} else {
			resp.send('FAIL');
		}
	});
});

myApp.listen(8000);
