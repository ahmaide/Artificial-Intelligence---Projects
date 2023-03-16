import pandas as pd
import matplotlib.pyplot as plt
from sklearn.feature_selection import SelectKBest, chi2
from sklearn.metrics import *
from sklearn.naive_bayes import BernoulliNB
from sklearn.naive_bayes import GaussianNB
from sklearn.naive_bayes import MultinomialNB
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn import tree
from sklearn.ensemble import RandomForestClassifier
from sklearn.neural_network import MLPClassifier
import joblib

from sklearn.tree import export_graphviz
from six import StringIO
import nltk.tokenize
import string
import re
from nltk.stem import PorterStemmer
import preprocess
import random

# negativeTweets = pd.read_csv('data/Negative+Tweets.tsv', sep="\t", header=None, encoding='utf-8')
# positiveTweets = pd.read_csv('data/Positive+Tweets.tsv', sep='\t', header=None, encoding='utf-8')
# fullDS = pd.concat([negativeTweets, positiveTweets], ignore_index=True)
# #print(negativeTweets.head())
# #print(positiveTweets.head())
#
# fullDS.columns = ["Type", "Tweet"]
# fullDS.to_csv("data/fullDS.csv")

data = preprocess.preprocess('data/fullDS.csv')
f = open('data/features.txt', 'w')
f.writelines("\n".join(data.columns))
f.close()
x = data.drop('Type', axis=1)
y = data['Type']
# h = SelectKBest(chi2, k=500)
# new_x = h.fit_transform(x, y)
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.25)
print("0 done")


def test(model, title, filename):
    model.fit(x_train.values, y_train.values)
    joblib.dump(model, filename)
    y_pred = model.predict(x_test.values)
    print("1 done")
    y_pred_prob = model.predict_proba(x_test.values)
    print("Sensitivity (recall) score: ", recall_score(y_test, y_pred))
    print("precision score: ", precision_score(y_test, y_pred))
    print("f1 score: ", f1_score(y_test, y_pred))
    print("accuracy score: ", accuracy_score(y_test, y_pred))
    print("ROC AUC: {}".format(roc_auc_score(y_test, y_pred_prob[:, 1])))

    RocCurveDisplay.from_estimator(model, x_test, y_test)
    plt.title(title)
    plt.show()
    DetCurveDisplay.from_estimator(model, x_test, y_test)
    plt.title(title)
    plt.show()
    ConfusionMatrixDisplay.from_estimator(model, x_test, y_test)
    plt.title(title)
    plt.show()
    PrecisionRecallDisplay.from_estimator(model, x_test, y_test)
    plt.title(title)
    plt.show()


model_rf = RandomForestClassifier()
test(model_rf, "Random Forest", "models/model_rf.sav")

print("t")
rf_validation = cross_val_score(model_rf, x, y, cv=5)
print("Scores using 5-fold cross validation: {}".format(rf_validation))
print("Average score using 5-fold cross validation: {:.3f}".format(rf_validation.mean()))
print("t")

model_nb = BernoulliNB()
test(model_nb, 'Bernoulli Naive bayes', "models/model_nb.sav")
nb_validation = cross_val_score(model_nb, x, y, cv=5)
print("Scores using 5-fold cross validation: {}".format(nb_validation))
print("Average score using 5-fold cross validation: {:.3f}".format(nb_validation.mean()))

model_nn = MLPClassifier(hidden_layer_sizes=200)
test(model_nn, 'Multi-layer Perceptron classifier', "models/model_nn.sav")
nn_validation = cross_val_score(model_nn, x, y, cv=5)
print("Scores using 5-fold cross validation: {}".format(nn_validation))
print("Average score using 5-fold cross validation: {:.3f}".format(nn_validation.mean()))