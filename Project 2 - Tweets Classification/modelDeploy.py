import joblib
import pandas as pd
import numpy as np
import preprocess
import numpy as np
with open('data/deployDS.txt', 'r', encoding="utf8") as f:
    originalDS = f.readlines()
originalDS = originalDS[1:]
f.close()
deployDS = pd.read_csv(r'data/deployDS.txt')
deployDS.columns = ['Tweet']
with open('data/features.txt', 'r') as f:
    t = f.readlines()
t = [i.replace('\n', '') for i in t]
t.remove('Type')
for x in t:
    deployDS[str(x)] = 0

deployDS.fillna(0)
deployDS.to_csv(r'data/deployDS.csv', index=None)

data = preprocess.preprocessTest('data/deployDS.csv')

model_rf = joblib.load('models/model_rf.sav')
model_rf_output = model_rf.predict(data.values)
f = open('deployment/rf_deploy.txt', 'w', encoding="utf-8")
rf_out = [originalDS, list(model_rf_output)]
rf_out = np.apply_along_axis(', '.join, 0, rf_out)
f.writelines("\n".join(rf_out))
f.close()


model_nb = joblib.load('models/model_nb.sav')
model_nb_output = model_nb.predict(data.values)
f = open('deployment/rf_deploy.txt', 'w', encoding="utf-8")
nb_out = [originalDS, list(model_nb_output)]
nb_out = np.apply_along_axis(', '.join, 0, nb_out)
f.writelines("\n".join(nb_out))
f.close()

model_nn = joblib.load('models/model_nn.sav')
model_nn_output = model_nn.predict(data.values)
f = open('deployment/nn_deploy.txt', 'w', encoding="utf-8")
nn_out = [originalDS, list(model_nn_output)]
nn_out = np.apply_along_axis(', '.join, 0, nn_out)
f.writelines("\n".join(nn_out))
f.close()
