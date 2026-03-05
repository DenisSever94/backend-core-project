@template.layout.main(
content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Добавить новый статус</h1>

        <form action="/leads/status" method="post" class="space-y-4">
            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">
                    Статус
                </label>
                <input
                        type="text"
                        id="status"
                        name="status"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="Новый"
                />
            </div>
            <button
                    type="submit"
                    class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-900"
            >
                Создать статус
            </button>
            <a href="/leads/status"
               class="block text-center text-sm text-gray-600 hover:text-gray-900">
                Отмена
            </a>
        </form>
    </div>
`
)