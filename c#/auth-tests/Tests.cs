namespace auth_tests;

using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using auth;

[TestClass]
public class Tests
{
    [TestMethod]
    public async Task TestTokenAsync()
    {
        string? scopes = Environment.GetEnvironmentVariable("SCOPES");
        string? client_id = Environment.GetEnvironmentVariable("CLIENT_ID");
        string? key = Environment.GetEnvironmentVariable("PRIVATE_KEY_FILE");

        Auth auth = new(client_id, key);

        string token = await auth.getAccessTokenAsync(scopes);
        Assert.IsTrue(Regex.IsMatch(token, @"^.{90}\..{300,}\..{342}$"));

    }
}
