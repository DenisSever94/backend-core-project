@import ru.mentee.power.crm.domain.Lead
@import ru.mentee.power.crm.domain.LeadStatus
@param Lead lead

<!DOCTYPE html>
<html>
<head>
    <title>Редактирование лида</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <h1 class="text-2xl font-bold mb-4">Редактирование лида</h1>
    <form method="post" action="/leads/${lead.id().toString()}">

        <!-- Скрытое поле ID -->
        <input type="hidden" name="id" value="${lead.id().toString()}">

        <!-- Поле company -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Company</label>
            <input type="text" name="company" value="${lead.company()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md">
        </div>

        <!-- Поле email -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input type="email" name="email" value="${lead.contact().email()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md">
        </div>

        <!-- Поле phone -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
            <input type="tel" name="phone" value="${lead.contact().phone()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md">
        </div>

        <!-- Поле city -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">City</label>
            <input type="tel" name="city" value="${lead.contact().address().city()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md">
        </div>

        <!-- Поле street -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Street</label>
            <input type="tel" name="street" value="${lead.contact().address().street()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md">
        </div>

        <!-- Поле zip -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Zip</label>
            <input type="tel" name="zip" value="${lead.contact().address().zip()}"
                   class="w-full px-3 py-2 border border-gray-300 rounded-md">
        </div>

        <!-- Статус - выпадающий список -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
            <select name="status" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                @if(lead.status() ==
  ru.mentee.power.crm.domain.LeadStatus.NEW)
                    <option value="NEW" selected>NEW</option>
                @else
                    <option value="NEW">NEW</option>
                @endif
                @if(lead.status() ==
  ru.mentee.power.crm.domain.LeadStatus.CONTACTED)
                    <option value="CONTACTED"
                            selected>CONTACTED</option>
                @else
                    <option value="CONTACTED">CONTACTED</option>
                @endif
                @if(lead.status() ==
 ru.mentee.power.crm.domain.LeadStatus.QUALIFIED)
                    <option value="QUALIFIED"
                            selected>QUALIFIED</option>
                @else
                    <option value="QUALIFIED">QUALIFIED</option>
                @endif
            </select>
        </div>

        <!-- Кнопки -->
        <div class="flex gap-2">
            <button type="submit"
                    class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
                Сохранить
            </button>
            <a href="/leads"
               class="bg-gray-300 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-400">
                Отмена
            </a>
        </div>

    </form>
</div>
</body>
</html>