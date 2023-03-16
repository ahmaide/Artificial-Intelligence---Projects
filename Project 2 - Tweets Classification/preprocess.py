import re
import string
import nltk
from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
import emoji
from ar_corrector.corrector import Corrector
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as N
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

stemmer = PorterStemmer()
sid = SentimentIntensityAnalyzer()
corr = Corrector()
stopWords = set(stopwords.words("arabic"))

tashkeel = re.compile(""" ّ    | # Tashdid
                             َ    | # Fatha
                             ً    | # Tanwin Fath
                             ُ    | # Damma
                             ٌ    | # Tanwin Damm
                             ِ    | # Kasra
                             ٍ    | # Tanwin Kasr
                             ْ    | # Sukun
                             ـ     # Tatwil/Kashida
                         """, re.VERBOSE)
emoj = re.compile("["
        u"\U0001F600-\U0001F64F"  # emoticons
        u"\U0001F300-\U0001F5FF"  # symbols & pictographs
        u"\U0001F680-\U0001F6FF"  # transport & map symbols
        u"\U0001F1E0-\U0001F1FF"  # flags (iOS)
        u"\U00002500-\U00002BEF"  # chinese char
        u"\U00002702-\U000027B0"
        u"\U00002702-\U000027B0"
        u"\U000024C2-\U0001F251"
        u"\U0001f926-\U0001f937"
        u"\U00010000-\U0010ffff"
        u"\u2640-\u2642" 
        u"\u2600-\u2B55"
        u"\u200d"
        u"\u23cf"
        u"\u23e9"
        u"\u231a"
        u"\ufe0f"  # dingbats
        u"\u3030"
                      "]+", re.UNICODE)

def extractEmoji(text):
    return " ".join(N.unique(re.findall(emoji.get_emoji_regexp(), text)))


def countEmojis(text):
    return len(N.unique(re.findall(emoji.get_emoji_regexp(), text)))


def getPosScore(emojisList):
    return sid.polarity_scores(emojisList).get('pos')


def getNegScore(emojisList):
    return sid.polarity_scores(emojisList).get('neg')


def getNeuScore(emojisList):
    return sid.polarity_scores(emojisList).get('neu')


def getCompoundScore(emojisList):
    return sid.polarity_scores(emojisList).get('compound')


def cleanText(text):
    clearedText = re.sub(r'[^\u0600-\u06FF ]+', '', text)
    tokens = nltk.word_tokenize(clearedText)
    words = [stemmer.stem(w) for w in tokens if w.replace(" ", "") not in stopWords]
    #words = [corr.contextual_correct(w) for w in words]
    return N.unique(words)


def clear(text):
    # removeLongation
    text = re.sub("[إأآا]", "ا", text)
    text = re.sub("ى", "ي", text)
    text = re.sub("ؤ", "ء", text)
    text = re.sub("ئ", "ء", text)
    text = re.sub("ة", "ه", text)
    text = re.sub("گ", "ك", text)

    # removeTashkeel
    text = re.sub(tashkeel, '', text)

    # removeConsecutiveChars
    text = re.sub(r'(.)\1+', r"\1", text)
    return text


def hashtags(message):
    hash = re.findall("#(\\w+)", message)
    return hash


def removeHashtags(message):
    return re.sub("(\\s*#(\\w+)\\s*)+", " ", message)


def removeEmojis(tweet):
    return re.sub(emoj, '', tweet)


def length(tweet):
    return len(tweet) if len(tweet) != 0 else 1


def display(data, text):
    sns.barplot(x='Type', y=text, data=data)
    sns.despine()
    plt.tight_layout()
    plt.show()


def preprocess(url, output='data/preprocessedData.csv'):
    data = pd.read_csv(url)
    data["emoji"] = data["Tweet"].apply(extractEmoji)

    # data["pos"] = data["emoji"].apply(getPosScore)
    # display(data, "pos")
    # data["neg"] = data["emoji"].apply(getNegScore)
    # display(data, "neg")
    # data["neu"] = data["emoji"].apply(getNeuScore)
    # display(data, "neu")
    # data["compound"] = data["emoji"].apply(getCompoundScore)
    # display(data, "compound")
    data["emojiCount"] = data["Tweet"].apply(countEmojis)
    display(data, "emojiCount")
    data["Tweet"] = data["Tweet"].apply(removeEmojis)
    print("Emoji done")


    data["hashtagsCount"] = data["Tweet"].apply(hashtags).apply(len)
    display(data, "hashtagsCount")
    data["Tweet"] = data["Tweet"].apply(removeHashtags)
    print("hashtagsCount done")

    data["sentencesCount"] = data["Tweet"].apply(nltk.sent_tokenize).apply(length)
    display(data, "sentencesCount")
    print("sentencesCount done")
    data["charsCount"] = data["Tweet"].apply(len)
    display(data, "charsCount")
    print("charsCount done")
    data["wordsCount"] = data["Tweet"].apply(cleanText).apply(length)
    display(data, "wordsCount")
    print("wordsCount done")
    data["hashtagsPercentage"] = round(data["hashtagsCount"] / (data["wordsCount"] + data["hashtagsCount"]), 3)
    display(data, "hashtagsPercentage")
    print("hashtagsPercentage done")
    data["emojiPercentage"] = round(data["emojiCount"] / (data["wordsCount"] + data["emojiCount"]), 3)
    display(data, "emojiPercentage")
    print("emojiPercentage done")
    data['avgSentenceLength'] = round(data['wordsCount'] / data['sentencesCount'], 3)
    display(data, "avgSentenceLength")
    print("avgSentenceLength done")
    data['avgWordLength'] = round(data['charsCount'] / data['wordsCount'], 3)
    display(data, "avgWordLength")
    print("avgWordLength done")
    data["stopwordsCount"] = data["Tweet"].apply(lambda words: sum([1 for word in words if word in stopWords]))
    display(data, "stopwordsCount")
    print("stopwordsCount done")
    try:
        data["Type"] = data["Type"].apply(lambda x: x == "pos")
    except:
        print()
    data["Tweet"] = data["Tweet"].apply(clear)



    vectorizer = TfidfVectorizer(token_pattern=r'[^\s]+', max_features=2000)
    x = vectorizer.fit_transform(data["emoji"])
    print(x.shape)
    x = pd.DataFrame.sparse.from_spmatrix(x)
    custom_feature_names = ["tfidf_" + str(feature) for feature in x.columns]
    x = x.astype(np.float64)
    x.set_axis(custom_feature_names, axis="columns", inplace=True)
    data = pd.concat([data, x], axis=1)
    print("emoji auto done")

    vectorizer = TfidfVectorizer(tokenizer=cleanText, max_features=100)
    x = vectorizer.fit_transform(data["Tweet"])
    x = pd.DataFrame.sparse.from_spmatrix(x)
    custom_feature_names = ["tfidf1_" + str(feature) for feature in x.columns]
    x = x.astype(np.float64)
    x.set_axis(custom_feature_names, axis="columns", inplace=True)
    data = pd.concat([data, x], axis=1)
    print("tweet auto done")

    data = data.drop(['Unnamed: 0', 'Tweet', 'emoji'], axis=1)
    data = data.fillna(0)
    data["emojiCount"] = data["emojiCount"].astype(np.float64)
    data["hashtagsCount"] = data["hashtagsCount"].astype(np.float64)
    data["sentencesCount"] = data["sentencesCount"].astype(np.float64)
    data["charsCount"] = data["charsCount"].astype(np.float64)
    data["wordsCount"] = data["wordsCount"].astype(np.float64)
    data["hashtagsPercentage"] = data["hashtagsPercentage"].astype(np.float64)
    data['avgWordLength'] = data['avgWordLength'].astype(np.float64)
    data['avgSentenceLength'] = data['avgSentenceLength'].astype(np.float64)
    data.to_csv(output, index=False)
    return data

def preprocessTest(url, output='data/preprocessedData.csv'):
    data = pd.read_csv(url)
    data["emoji"] = data["Tweet"].apply(extractEmoji)
    data["emojiCount"] = data["Tweet"].apply(countEmojis)
    data["Tweet"] = data["Tweet"].apply(removeEmojis)
    data["hashtagsCount"] = data["Tweet"].apply(hashtags).apply(len)
    data["Tweet"] = data["Tweet"].apply(removeHashtags)
    data["sentencesCount"] = data["Tweet"].apply(nltk.sent_tokenize).apply(length)
    data["charsCount"] = data["Tweet"].apply(len)
    data["wordsCount"] = data["Tweet"].apply(cleanText).apply(length)
    data["hashtagsPercentage"] = round(data["hashtagsCount"] / (data["wordsCount"] + data["hashtagsCount"]), 3)
    data["emojiPercentage"] = round(data["emojiCount"] / (data["wordsCount"] + data["emojiCount"]), 3)
    data['avgSentenceLength'] = round(data['wordsCount'] / data['sentencesCount'], 3)
    data['avgWordLength'] = round(data['charsCount'] / data['wordsCount'], 3)
    data["stopwordsCount"] = data["Tweet"].apply(lambda words: sum([1 for word in words if word in stopWords]))
    data["Tweet"] = data["Tweet"].apply(clear)

    vectorizer = TfidfVectorizer(token_pattern=r'[^\s]+', max_features=2000)
    x = vectorizer.fit_transform(data["emoji"])
    print(x.shape)
    x = pd.DataFrame.sparse.from_spmatrix(x)
    custom_feature_names = ["tfidf_" + str(feature) for feature in x.columns]
    foundF = [f for f in data.columns if f in custom_feature_names]
    print(foundF)
    print(len(data.columns))
    data = data.drop(foundF, axis=1)
    print(len(data.columns))


    x = x.astype(np.float64)
    x.set_axis(custom_feature_names, axis="columns", inplace=True)
    data = pd.concat([data, x], axis=1)

    vectorizer = TfidfVectorizer(tokenizer=cleanText, max_features=100)
    x = vectorizer.fit_transform(data["Tweet"])
    print(x.shape)
    x = pd.DataFrame.sparse.from_spmatrix(x)
    custom_feature_names = ["tfidf1_" + str(feature) for feature in x.columns]
    foundF1 = [f for f in data.columns if f in custom_feature_names]
    data = data.drop(foundF1, axis=1)
    x = x.astype(np.float64)
    x.set_axis(custom_feature_names, axis="columns", inplace=True)
    data = pd.concat([data, x], axis=1)

    data = data.drop(['Tweet', 'emoji'], axis=1)
    data = data.fillna(0)
    data["emojiCount"] = data["emojiCount"].astype(np.float64)
    data["hashtagsCount"] = data["hashtagsCount"].astype(np.float64)
    data["sentencesCount"] = data["sentencesCount"].astype(np.float64)
    data["charsCount"] = data["charsCount"].astype(np.float64)
    data["wordsCount"] = data["wordsCount"].astype(np.float64)
    data["hashtagsPercentage"] = data["hashtagsPercentage"].astype(np.float64)
    data['avgWordLength'] = data['avgWordLength'].astype(np.float64)
    data['avgSentenceLength'] = data['avgSentenceLength'].astype(np.float64)
    data.to_csv(output, index=False)
    return data

    # data['avgWordLength'] = round(data['charsCount'] / data['wordsCount'], 3)
    # sns.barplot(x='Type', y='avgWordLength', data=data)
    # sns.despine()
    # plt.tight_layout()
    # plt.show()
    # data["Tweet"] = data["Tweet"].apply(cleanText2)
    # data['avgCharPerWord'] = data['Tweet'].apply(lambda x: avg_word(x))
    # vectorizer = CountVectorizer(tokenizer=nltk.sent_tokenize, token_pattern=None, max_features=int(150))
