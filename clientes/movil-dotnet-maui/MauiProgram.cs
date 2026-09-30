namespace Arquitectura.ClienteMovilMaui;

public static class MauiProgram
{
    public static MauiApp CreateMauiApp()
    {
        var builder = MauiApp.CreateBuilder();
        builder
            .UseMauiApp<App>()
            .ConfigureFonts(fonts =>
            {
                fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
            });

        builder.Services.AddSingleton(new HttpClient { BaseAddress = new Uri("http://10.0.2.2:5100") });
        builder.Services.AddSingleton<MainPage>();
        return builder.Build();
    }
}
