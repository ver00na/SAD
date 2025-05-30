### Краткий конспект лекции по алгоритму пчелиной колонии и алгоритму светлячков

#### **1. Алгоритм пчелиной колонии (ABC)**
**Основные идеи:**
- Основан на поведении медоносных пчел при поиске нектара.
- Пчелы делятся на три группы:
  - **Активные фуражиры** — собирают нектар из известных источников.
  - **Разведчики** (10% фуражиров) — исследуют новые участки.
  - **Неактивные фуражиры** — ждут информации от других пчел (через "виляющий танец").

**Этапы алгоритма:**
1. **Инициализация**:  
   - Случайный выбор точек (координат) для пчел-разведчиков.  
   - Вычисление значения целевой функции в этих точках.  
2. **Выбор участков**:  
   - Лучшие участки (с максимальным значением функции).  
   - Перспективные участки (близкие к лучшим).  
3. **Локальный поиск**:  
   - Вокруг лучших точек создаются гиперкубы (области поиска).  
   - Новые пчелы отправляются в эти области для уточнения решения.  
4. **Обновление**:  
   - Повторение шагов 2-3 до достижения критерия останова (например, неизменность результата в течение нескольких итераций).  

**Пример**:  
Для функции
```math
f(x, y) = -(x^2 + y^2)
```
алгоритм находит глобальный максимум в точке (0, 0).  

**Ключевые параметры**:  
- Количество разведчиков (S).  
- Размер области локального поиска (Δ).  
- Количество лучших и перспективных участков.  

---

#### **2. Алгоритм светлячков (Firefly Algorithm)**
**Основные идеи**:  
- Основан на поведении светлячков, которые привлекают друг друга яркостью свечения.  
- Яркость свечения ассоциируется со значением целевой функции.  

**Этапы алгоритма**:  
1. **Инициализация**:  
   - Случайное распределение агентов (светлячков) в пространстве поиска.  
   - Назначение начального уровня люциферина (яркости).  
2. **Обновление яркости**:  
   - Зависит от значения целевой функции:  
     ```math
     l_k(t+1) = (1 - \rho)l_k(t) + \gamma F(x_k(t+1)).
     ```  
3. **Перемещение агентов**:  
   - Агенты движутся в сторону более ярких соседей.  
   - Вероятность перемещения вычисляется на основе разницы яркостей.  
4. **Коррекция радиуса поиска**:  
   - Радиус окрестности уменьшается при высокой плотности агентов и увеличивается при низкой.  

**Преимущества**:  
- Высокая точность.  
- Автоматическая адаптация радиуса поиска.  

**Недостатки**:  
- Длительное время работы при большом количестве итераций.  

---

#### **3. Сравнение алгоритмов**
| **Критерий**          | **Алгоритм пчелиной колонии**       | **Алгоритм светлячков**             |
|-----------------------|-------------------------------------|-------------------------------------|
| **Основа**            | Поведение пчел                      | Поведение светлячков                |
| **Ключевые агенты**   | Разведчики, фуражиры               | Агенты с уровнем яркости            |
| **Поиск**            | Локальный и глобальный             | Направленный (к более ярким агентам)|
| **Параметры**        | S, Δ, τ                            | ρ,γ,β                               |
| **Применение**       | Оптимизация функций, TSP           | Глобальная оптимизация              |

**Вывод**:  
Оба алгоритма относятся к бионическим методам оптимизации и эффективны для решения сложных задач. ABC лучше подходит для задач с явными локальными экстремумами, а алгоритм светлячков — для глобальной оптимизации с адаптивным поиском.
