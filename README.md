Sous-Chef is an application that allows users to maintain a shopping list filled with ingredients for dishes of their choice.

AUTHORS: Roger Chapa, Meghana Guntaka, Suman Poudel, Maria Maldonado, Javier Gamez, Hunter Flory

This application was developed in Android Studios utilizing agile software methodology. 

/* CLASS FILES */

// Dish Search // 
This class utilizes RapidAPI to return search results from a user-inputted query. 
The user inputs the name of the dish they would like to make and a list of relevant dishes (titles of json objects) are returned to the user. 
The user can tap on a dish that they like and from there can view the recipe instructions via link to the recipe's website or can directly add the dish to their shopping cart. The API is used to get a list of all the ingredients required to make by parsing the json object into an array list. The parsed ingredients are then added to the user's shopping list once they have selected all the dishes they want to make and tapped 'Save List'.

// Pantry //
This class takes data from the saved shopping list and allows the user to view ingredients they have stored in their 'pantry'. This means that the user has completed 'shopping' and now have a pantry full of the ingredients for dishes they'd like to make. From here the user may edit the contents of the pantry or use the pantry class's 'Reverse Search' feature to find a list of dishes that use the ingredients in their pantry. For example, if the user decides that another dish sounds better than the one originally intended, a new dish may be found based on the ingredients the user has left. This class utilizes keys in Android Studio to save the contents of the pantry locally so data is not lost on exit.

// Shopping List //
This class saves data from Dish Search locally and passes data to Pantry. This is where the user stores their chosen dishes and ingredients. When the application is reopened, if Shopping List has any data stored in it, both Pantry and Dish Search load data from Shopping List.

// Welcome Screen //
This class allows the user to navigate the application by providing buttons to move to the Pantry, Shopping List, and Dish Search screens where the user can view and edit data.
