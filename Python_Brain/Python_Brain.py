import numpy as np
from matplotlib import pyplot as plt
import pandas as pd
import sys


class Csv_File:
    Data: pd.DataFrame()
    number_of_rows: int
    number_of_columns: int

    def __init__(self):
        self.Data = pd.DataFrame()
        self.number_of_rows = 0
        self.number_of_columns = 0

    def __str__(self):
        print(self.Data)
        return ""

    def load_csv(self, Path: str):
        try:
            self.Data = pd.read_csv(Path)
            self.number_of_columns = len(self.Data.columns)
            self.number_of_rows = len(self.Data)
        except:
            print("There Was An Error In File Loading ,Check File Path/Name\n Process Terminated")
            exit(12)

    def write_csv(self, Filename: str):
        try:
            self.Data = self.Data[:]
            self.Data.to_csv(Filename, index=False)
        except:
            print("There Was An Error In File Writing ,Check File Path/Name\n Process Terminated")
            exit(12)

    def get(self, Row: int, Column: str):
        try:
            return self.Data[Column].values.item(Row)
        except:
            print("There Was An Error In Fetching Value,Check Row/Column Numbers")
            exit(12)

    def get_row(self, Row: int):
        try:
            row = []
            for i in self.Data.columns:
                row.append(self.get(Row, i))
            return row
        except:
            print("Error In Row Number")
            exit(12)

    def add_row(self, values: list):
        if len(self.Data.columns) < len(values):
            print("There Are More Values In Sample Then Columns In File")
            exit(10)
        else:
            row = {}
            for i in range(0, len(self.Data.columns)):
                row[self.Data.columns[i]] = values[i]
        self.Data = self.Data.append(row, ignore_index=True)
        self.number_of_rows += 1

    def get_column_values(self, column_name: str):
        return self.Data[column_name].values

    def add_column(self, column_name: str, values):
        self.Data[column_name] = values
        self.number_of_columns += 1

    def set(self, Row: int, Column: str, val):
        try:
            self.Data[Column].values.itemset(Row, val)
        except:
            print("There Was An Error In Setting Value,Check Row/Column Numbers")
            exit(12)

    def remove_row(self, rows_to_drop: list):
        self.Data = self.Data.drop(rows_to_drop)
        self.number_of_rows -= len(rows_to_drop)

    def remove_column(self, column: str):
        self.Data = self.Data.drop(columns=column)
        self.number_of_columns -= 1

    def split_data(self, split_percentage: int):
        if split_percentage > 100 or split_percentage < 0:
            print("Split Precentage Must Be Between (0-100)")
            exit(12)
        else:
            amount_needed = np.round((self.number_of_rows / 100) * split_percentage)
            rng = np.random.default_rng()
            selected_rows = rng.choice(self.number_of_rows, size=int(amount_needed), replace=False)

            result = Csv_File()
            result.Data = pd.DataFrame()
            for i in self.Data.columns:
                result.add_column(i, [])
            for i in range(0, int(amount_needed)):
                result.add_row(self.get_row(selected_rows[i]))
            self.remove_row(selected_rows)
            self.Data = self.Data.reset_index(drop=True)
            return result

    def replace_pattern_in_column(self, column: str, pattern, replace_width):
        self.Data[column] = self.Data[column].replace(pattern, replace_width)

    def number_of_missing(self):
        return self.Data.isnull().sum().sum()


class Brain_Util:
    def twoD_matrix_to_csv(matrix: np.ndarray):
        shape = matrix.shape
        result = Csv_File()
        matrix = matrix.T
        for i in range(0, shape[0]):
            name = str.format("Column {}", i + 1)
            result.add_column(name, matrix[i])
        return result

    def sigmoid(self, x):
        return 1.0 / (1.0 + np.exp(-x))


class Python_Brain:
    Data_Set: Csv_File

    def load_data_set(self, data_set: Csv_File):
        self.Data_Set = data_set

    def pearson_correlation_coefficient(self, Y: np.ndarray, Y_HAT: np.ndarray):
        sigma_xy = (Y * Y_HAT)
        sigma_xy = sigma_xy.sum()
        sigma_x = Y.sum()
        sigma_y = Y_HAT.sum()
        sigma_xs = (Y * Y).sum()
        sigma_ys = (Y_HAT * Y_HAT).sum()
        r = len(y) * sigma_xy - sigma_x * sigma_y
        temp = len(Y) * sigma_xs - sigma_x * sigma_x
        temp *= len(Y) * sigma_ys - sigma_y * sigma_y
        r /= np.sqrt(temp)
        return r

    def rankify(self, array: np.ndarray):
        ranks = np.zeros((len(array)), dtype=float)
        for i in range(0, len(ranks)):
            r, s = 1, 1
            for j in range(0, i):
                if array.item(j) < array.item(i):
                    r += 1
                if array.item(j) == array.item(i):
                    s += 1
            for j in range(i + 1, len(array)):
                if array.item(j) < array.item(i):
                    r += 1
                if array.item(j) == array.item(i):
                    s += 1
            ranks[i] = (r + (s - 1) * 0.5)
        return ranks

    def spearman_correlation_coefficient(self, Y: np.ndarray, Y_HAT: np.ndarray):
        rankY = self.rankify(Y)
        rankYhat = self.rankify(Y_HAT)
        sum_X = rankY.sum()
        sum_Y = rankYhat.sum()
        sum_XY = (rankY * rankYhat).sum()
        squareSum_X = (rankY * rankY).sum()
        squareSum_Y = (rankYhat * rankYhat).sum()
        SCC = (len(Y) * sum_XY - sum_X * sum_Y) / np.sqrt(
            (len(Y) * squareSum_X - sum_X * sum_X) * (len(Y) * squareSum_Y - sum_Y * sum_Y))
        return SCC

    def compute_column_correlations(self, correlation_type):
        return self.Data_Set.Data.corr(method=correlation_type)

    def linear_regression_static_formula(self, column_a: str, column_b: str):
        sum_x = self.Data_Set.Data[column_a].values.sum()
        sum_y = self.Data_Set.Data[column_b].values.sum()
        sum_xy = (self.Data_Set.Data[column_a].values * self.Data_Set.Data[column_b].values).sum()
        sum_xsquared = (self.Data_Set.Data[column_a].values * self.Data_Set.Data[column_a].values).sum()
        sum_ysquared = (self.Data_Set.Data[column_b].values * self.Data_Set.Data[column_b].values).sum()
        result = np.zeros(2)
        result[0] = (sum_y * sum_xsquared - sum_x * sum_xy) / (
                    self.Data_Set.number_of_rows * sum_xsquared - sum_x * sum_x)
        result[1] = (self.Data_Set.number_of_rows * sum_xy - sum_x * sum_y) / (
                    self.Data_Set.number_of_rows * sum_xsquared - sum_x * sum_x)
        return result

    def k_means(self, features: list, k: int, number_of_iterations: int):
        means = np.zeros((k, len(features)))
        for cluster in range(0, len(means)):
            means[cluster] = [features[i].item(np.random.randint(0, len(features[i]))) for i in range(0, len(features))]
        assignments = np.zeros(len(features[0]))

        for iteration in range(0, number_of_iterations):
            # Find assignments
            for point in range(0, len(features[0])):
                best_distance = sys.float_info.max
                best_cluster = 0
                for cluster in range(0, k):
                    sample = np.array([features[i].item(point) for i in range(0, len(features))])
                    distance = np.linalg.norm(sample - means.item(cluster))
                    if distance < best_distance:
                        best_distance = distance
                        best_cluster = cluster
                assignments[point] = best_cluster
            # Sum up and count points for each cluster
            new_means = np.zeros((k, len(features)))
            counts = np.zeros(k)
            for point in range(0, len(features[0])):
                cluster = assignments.item(point)
                temp = np.zeros(len(features), dtype=np.float)
                for fet in range(0, len(features)):
                    temp.itemset(fet, (new_means.item(int(cluster)) + features[fet].item(point)))
                new_means[int(cluster)] = temp
                counts[int(cluster)] += 1
            for cluster in range(0, k):
                count = max(1, counts.item(int(cluster)))
                temp = np.zeros(len(features), dtype=np.float)
                means[int(cluster)] = new_means[int(cluster)] / count
        return means

    def get_mse(self, Y: np.ndarray, Y_HAT: np.ndarray):
        return ((Y - Y_HAT) ** 2).mean()

    def get_mae(self, Y: np.ndarray, Y_HAT: np.ndarray):
        return np.abs(Y - Y_HAT)

    def get_r_squared(self, Y: np.ndarray, Y_HAT: np.ndarray):
        PCC = self.pearson_correlation_coefficient(Y, Y_HAT)
        return PCC * PCC

    def get_adjusted_r_squared(self, Y: np.ndarray, Y_HAT: np.ndarray, Independent_Variables: int):
        R_S = self.get_r_squared(Y, Y_HAT)
        ARS = (1 - R_S) * (self.Data_Set.number_of_rows - 1)
        ARS /= self.Data_Set.number_of_rows - 1 - Independent_Variables
        ARS = 1.0 - ARS
        return ARS

    def confusion_matrix(self, regression_weights: np.ndarray, binary_column: str, sampled_rows: list,
                         decision_boundary: float):
        CM = np.zeros((2, 2), dtype=float)
        TruePositive, TrueNegative, FalsePositive, FalseNegative = 0, 0, 0, 0
        for i in range(0, self.Data_Set.number_of_rows):
            pred_s = regression_weights.item((0, 0))
            for j in range(0, sampled_rows + 1):
                pred_s += regression_weights[j, 0] * self.Data_Set.get(i, sampled_rows[j - 1])
            pred_s = 0 if pred_s < decision_boundary else 1
            actual = self.Data_Set.get(i, binary_column)
            if pred_s == 0 and actual == 0:
                TrueNegative += 1
            elif pred_s == 1 and actual == 0:
                FalsePositive += 1
            elif pred_s == 1 and actual == 1:
                TruePositive += 1
            elif pred_s == 0 and actual == 1:
                FalseNegative += 1
        CM[0, 0] = TruePositive
        CM[1, 0] = FalsePositive
        CM[0, 1] = FalseNegative
        CM[1, 1] = TrueNegative
        return CM

    def KNN(self,K:int ,test_values:list,sample_columns:list):
        distances = np.zeros(self.Data_Set.number_of_rows)
        x = np.zeros(len(sample_columns))
        result = np.zeros((K,2))
        for i in range(0,self.Data_Set.number_of_rows):
            for j in range(0,len(sample_columns)):
                x[j] = self.Data_Set.get(i,sample_columns[j])
            dist = np.linalg.norm(x - test_values)
            distances.itemset(i, dist)
        knn = np.zeros(K)
        min = sys.float_info.min
        for i in range(0,K):
            for j in range(0,len(distances)):
                if distances.item(j) < min:
                    min = distances.item(j)
                    knn[i] = j
            result[i,1] = min
            distances[int(knn[i]-1)]=np.inf
            min = sys.float_info.min
        for i in range(0,K):
            result[i,0] = knn[i]
        return result

    def step_gradient(self,Current_Weights,Learning_Rate,Columns_Of_Sampels,True_Y):
        nof = len(Columns_Of_Sampels)+1
        gradient = np.zeros((nof,1))
        teta = np.array(Current_Weights)
        values = np.zeros((nof,1))
        prediction = np.array(Current_Weights)
        teta = teta.T

        for i in range(1,self.Data_Set.number_of_rows):
            values[0,0] = 1
            for j in range(1,nof):
                values[j,0] = self.Data_Set.get(i,Columns_Of_Sampels[j-1])
            h0 = teta*values
            for j in range(0,nof):
                gradient[j,0] += (h0[0,0] - self.Data_Set.get(i,True_Y)*values[j,0])
        for j in range(0,nof):
            gradient[j,0] *=Learning_Rate/self.Data_Set.number_of_rows
            prediction[j,0] = prediction[j,0] -gradient[j,0]
        return  prediction

    def linear_regression_gradient_descent(self,sample_columns:list,true_y,leaning_rate,number_of_iter):
        LL = np.zeros((len(sample_columns)+1,1))
        for i in range(number_of_iter):
            LL = self.step_gradient(LL,leaning_rate,sample_columns,true_y)
        return LL


z = Csv_File()
z.load_csv("30.csv")

b = Python_Brain()
b.load_data_set(z)

y = b.Data_Set.get_column_values('X1')
yh = b.Data_Set.get_column_values('X2')

print(b.linear_regression_gradient_descent(['X1'],['X2'],0.0003,100))