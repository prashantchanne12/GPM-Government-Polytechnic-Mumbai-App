'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.firestore.document("Users/{user_id}/notification/{notification_id}").onWrite((change, context) => {

	const user_id = context.params.user_id;
	const notification_id = context.params.notification_id;

	console.log("User Id: "+ user_id + "  | Notification Id: "+ notification_id);

	return admin.firestore().collection("Users").doc(user_id).collection("notification").doc(notification_id).get().then(queryResult => {

		const from_user_id = queryResult.data().from;
		const from_message = queryResult.data().message;	


		const from_data = admin.firestore().collection("Users").doc(from_user_id).get();   //Getting the data who send the notication.
		const to_data = admin.firestore().collection("Users").doc(user_id).get();		  //Getting thr data who recieved the notification.

	

		return Promise.all([from_data, to_data]).then(result => {

				//if(result.exists){
			const from_name = result[0].data().name;
			const to_name = result[1].data().name;
			const token_id = result[1].data().token_id;

			//return console.log("From: "+ from_name + "  To: "+to_name);
		
				const payload = {
					notification: {

						title : "Notification from : " +from_name,
						body : from_message,
						icon : "default",
						click_action : "com.prashantchanne.chatbox.firebasenotification.TARGETNOTIFICATION"	

					}, 

					data : {
						message : from_message,
						from_user_id : from_user_id
					}
				};

				return admin.messaging().sendToDevice(token_id, payload).then(result => {

						return console.log("Notification sent");

				});

		//}else{

		//	throw new Error("Does not exists");
		//}

		});

	});

});