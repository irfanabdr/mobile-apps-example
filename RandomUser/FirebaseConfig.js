import * as firebase from "firebase";

// Initialize Firebase
const firebaseConfig = {
  apiKey: "AIzaSyC9KbVYKPgJVmcI9Gaoek89f5v44n5qt3Q",
  authDomain: "randomuser-3d919.firebaseapp.com",
  databaseURL: "https://randomuser-3d919.firebaseio.com",
  projectId: "randomuser-3d919",
  storageBucket: "",
  messagingSenderId: "1021050166176",
  appId: "1:1021050166176:web:f1b948fe2a7b7b84"
};

export const firebaseApp = firebase.initializeApp(firebaseConfig);
