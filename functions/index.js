const functions = require('firebase-functions');

const admin = require('firebase-admin');

const request = require('request-promise')

admin.initializeApp(functions.config().firebase);

exports.indexPostsToELastic = functions.database.ref('/cars/{post_id}')
    .onWrite((change,context) =>{
        let postData = change.after.val();
        let post_id = context.params.post_id;

        console.log('Indexing post:',postData);

        let elasticSearchConfig = "http://34.66.217.199/elasticsearch/";
        let elasticSearchUrl = elasticSearchConfig + 'cars/car/' + post_id;
        let elasticSearchMethod = postData ? 'POST' : 'DELETE';

        let elasticSearchRequest = {
            method: elasticSearchMethod,
            url: elasticSearchUrl,
            auth: {
                username: "user",
                password: "gawpxuT5J2mT",
            },
            body: postData,
            json: true
          };


    return request(elasticSearchRequest).then(response => {
        console.log("ElastiicSearch response", response);
        return null;
    });

    });



exports.helloWorld = functions.https.onRequest((req, res) => {
    res.send("Hello from Firebase!");
});


 .addCar = functions.https.onRequest((req, res) => {


const brand = req.body.brand;
const model = req.body.model;
const price = req.body.price;



	return	admin.database().ref('/cars').push({brand: brand, model: model, price: price }).then((snapshot) => {

      return res.redirect(303, snapshot.ref);
  });
});




function createCar(req, response) {
const car = new Car({
	brand: req.body.brand,
	model: req.body.model,
});

car.save((err, car)=> {
	if(err)
	   response.send('could not save');
	response.json(car);
});
}
exports.car = functions.https.onRequest((req, res) => {
if(request.method === 'POST')
	createCar(req,res);
});
