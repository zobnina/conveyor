## Реализация кредитного конвейера

### DTO:
- LoanApplicationRequestDTO
- LoanOfferDTO
- ScoringDataDTO
- CreditDTO
- EmploymentDTO
- PaymentScheduleElement

### API:
- `POST: /conveyor/offers` - расчёт возможных условий кредита. Request - LoanApplicationRequestDTO, response - List<LoanOfferDTO>. 
  На основании LoanApplicationRequestDTO происходит прескоринг создаётся 4 кредитных предложения LoanOfferDTO 
  на основании всех возможных комбинаций булевских полей isInsuranceEnabled и isSalaryClient
  (false-false, false-true, true-false, true-true). Логику формирования кредитных предложений можно придумать самому. 
  К примеру: в зависимости от страховых услуг увеличивается/уменьшается процентная ставка и сумма кредита, 
  базовая ставка хардкодится в коде через property файл. Например цена страховки 100к 
  (или прогрессивная, в зависимости от запрошенной суммы кредита), ее стоимость добавляется в тело кредита, но она уменьшает ставку на 3. 
  Цена зарплатного клиента 0, уменьшает ставку на 1.
  Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему" (чем меньше итоговая ставка, тем лучше).
  POST: /conveyor/calculation

- `POST: /conveyor/calculation` - валидация присланных данных + скоринг данных + полный расчет параметров кредита. 
  Request - ScoringDataDTO, response CreditDTO. Происходит скоринг данных, высчитывание ставки(rate), 
  полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей 
  (List<PaymentScheduleElement>). Логику расчета параметров кредита можно найти в интернете, 
  полученный результат сверять с имеющимися в интернете калькуляторами графиков платежей и ПСК.
  Ответ на API - CreditDTO, насыщенный всеми рассчитанными параметрами.

### Stack

- Spring Boot
- Maven
- openapi generator
- Lombok
- JUnit5
